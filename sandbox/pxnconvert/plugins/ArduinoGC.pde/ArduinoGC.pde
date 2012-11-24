//###################################################
//#    Controller Software
//#      for GrowControl
//#        by PoiXson
//#
//# Supported Software Versions:
//#   GrowControl 3.0
//#   GrowControl 1.4
//#
//# Version:
//#   1.4.0 rev 630 - updated etherShield library to latest:
//#                   https://github.com/thiseldo/EtherShield
//#                 - simplified the commands list
//#                 - full support for USB/Serial, WizNet,
//#                   and ENC28j60 EtherShield
//#                 - warning: using eitherShield, tcp data sent
//#                   to device must be at least 8 chars long
//#   1.3.6 rev 505 - updated etherShield library to latest:
//#                   https://github.com/jmccrohan/EtherShield
//#   1.3.5 rev 484 - removed () and = from returned commands
//#   1.3.4 rev 464 - added monitor and pin read commands
//#                 - changed ethernet to use existing
//#                   protocol instead of http
//#   1.3.3 rev 433 - added etherShield support
//#                 - renamed to ArduinoGC
//#   1.3.2 rev 308 - added messenger library locally
//#   1.3.1 rev 279 - added reset and name commands
//#   1.3.0 rev 223 - Initial release
//#
//# Uses:
//#  * support for USB/Serial, WizNet Ethernet, and
//#    and ENC28j60 EtherShield for communication
//#  * controls input and output of pins
//###################################################
#define VERSION "gc1.4.0.630"


// (uncomment only one)
//#define UsingWizNet      // WIZ5100
//#define UsingEtherShield // ENC28j60

// networking
//#if defined(UsingWizNet) || defined(UsingEtherShield)
static byte     eth_mac[6] = {0x54,0x55,0x58,0x10,0x00,0x50};
static byte      eth_ip[4] = {192,168,3,110}; 
static byte eth_gateway[4] = {192,168,3,1};
static byte  eth_subnet[4] = {255,255,255,0};
#define eth_port 23
//#endif


// ##################################################
// COMMANDS
// ========
//
// status led (pin 13)
// --------------------
// led 255         - always on
// led 0           - always off
// led n           - set to binary pattern (0-255)
// --------------------
// returns: led n
//
// reset
// --------------------
// ~               - reinitialize (simulates a reset)
//
// device name
// --------------------
// name            - reads the current device name
// name <name>     - sets the device name to "<name>"
// --------------------
// returns: name <name>
//
// pin modes
// --------------------
// mode p          - reads the current pin mode
// mode p x        - disabled / no function
// mode p io       - digital I/O output
// mode p pwm      - digital pwm output (0-255)
// mode p in       - digital input (floating)
// mode p inh      - digital input (pulled high)
// mode p analog   - analog input (0-1023)
// --------------------
// p = pin number
// returns: mode p <mode>
//
// pin states
// --------------------
// pin p           - reads the current pin state
// pin n [1|on]    - set io output on
// pin n [0|off]   - set io output off
// pin p x         - set pwm output to x
// --------------------
// p = pin number
// returns: pin p x
//
// ##################################################


// send outgoing ethernet data to serial also
#define ethPrintToSerial

// usable pins
#if defined(UsingWizNet) || defined(UsingEtherShield)
#define PIN_START 3
#define PIN_LAST 19
#else
#define PIN_START 2
#define PIN_LAST 19
#endif

// memory locations
#define MEMORY_STATUSLED 0
#define MEMORY_MODES     1+MEMORY_STATUSLED
#define MEMORY_NAME    200

// pin modes
#define MODE_DISABLED 0
#define MODE_IO       1
#define MODE_PWM      2
#define MODE_IN       3
#define MODE_INH      4
#define MODE_ANALOG   5

// pin arrays
byte pin_modes  [PIN_LAST+1];
byte pin_states [PIN_LAST+1];


// messenger library
#include <Messenger.h>
Messenger msg=Messenger();

// EEPROM library
#include <EEPROM.h>

//#include <Streaming.h>


// status led
#if !defined(UsingWizNet) && !defined(UsingEtherShield)
byte StatusLedPattern   =0;
byte tmpStatusLedPattern=0;
byte StatusLedPosition  =0;
#endif
unsigned long tmrLast   =0;


void setup(){
  Serial.begin(115200);
  msg.attach(process);
  ethLayer_setup();
  reset();
}


void reset(){
  tmrLast=millis();
#if !defined(UsingWizNet) && !defined(UsingEtherShield)
  // status led
  StatusLedPosition=0;
  StatusLedPattern=255-EEPROM.read(MEMORY_STATUSLED);
#endif
  // init
  eth_println();
  ReadVersion();
  ReadName();
  // init pins
  eth_print("pins "); eth_print  (int(PIN_START));
  eth_print('-');     eth_println(int(PIN_LAST));
  for(byte t=PIN_START;t<PIN_LAST+1;t++){
    // load pin mode
    SetMode(t,EEPROM.read(MEMORY_MODES+t));
    ReadMode(t);
    ReadPin(t);
  } eth_println("READY");
}


void loop(){
  ethLayer_loop();
  // tick delay
  if((millis()-tmrLast)>=500){
    tmrLast=millis();
//    // monitor pins
//    for(byte t=PIN_START;t<PIN_LAST+1;t++){
//      if(pin_monitor[t]){readPinState(t);}
//    }
// status led pattern
#if !defined(UsingWizNet) && !defined(UsingEtherShield)
    if(StatusLedPosition==0){tmpStatusLedPattern=StatusLedPattern;}
    if(tmpStatusLedPattern & B10000000){digitalWrite(13,HIGH);}else{digitalWrite(13,LOW);}
    tmpStatusLedPattern=tmpStatusLedPattern<<1;
    StatusLedPosition++; if(StatusLedPosition>7){StatusLedPosition=0;}
#endif
  }
}


void GotData(){
  while( ethLayer_available() ){
    msg.process(ethLayer_read());
  } msg.process('\r');
  ethLayer_send();
  ethLayer_clear();
}


// process commands
void process(){
  if(!msg.available()){return;}
#if !defined(UsingWizNet) && !defined(UsingEtherShield)
  digitalWrite(13,HIGH);
#endif

  // pin mode
  if(msg.checkString("mode")){
    int pin=msg.readInt();
    if(msg.available()){
      // disabled
      if(msg.checkString("x")){
        SetMode(pin,MODE_DISABLED);
      // io output
      }else if(msg.checkString("io")){
        SetMode(pin,MODE_IO);
      // pwm
      }else if(msg.checkString("pwm")){
        SetMode(pin,MODE_PWM);
      // input
      }else if(msg.checkString("in")){
        SetMode(pin,MODE_IN);
      // input high
      }else if(msg.checkString("inh")){
        SetMode(pin,MODE_INH);
      // analog
      }else if(msg.checkString("analog")){
        SetMode(pin,MODE_ANALOG);
      }
    }
    // return/read mode
    ReadMode(pin);

  // pin state
  }else if(msg.checkString("pin")){
    int pin=msg.readInt();
    if(msg.available()){
      // on
      if(msg.checkString("on")){
        pin_states[pin]=255;
      // off
      }else if(msg.checkString("off")){
        pin_states[pin]=0;
      // pwm value
      }else{
        pin_states[pin]=msg.readInt();
      } SetPin(pin);
    }
    // return/read pin state
    ReadPin(pin);

  // status led
#if !defined(UsingWizNet) && !defined(UsingEtherShield)
  }else if(msg.checkString("led")){
    StatusLedPattern=msg.readInt();
    StatusLedPosition=0;
    Serial.print("led ");
    Serial.print(StatusLedPattern,DEC);
    Serial.print('/');
    Serial.println(StatusLedPattern,BIN);
    EEPROM.write(MEMORY_STATUSLED,255-StatusLedPattern);
#endif

  // device name
  }else if(msg.checkString("name")){
    WriteName();
    ReadName();

  // clear eeprom
  }else if(msg.checkString("clear")){
    if(msg.checkString("eeprom")){
      Serial.println();
      for(int t=0;t<512;t++){
        EEPROM.write(t,255);
        if(t%32==0){Serial.print('.');}
      } Serial.println();
      eth_println("Please reset device now.");
#if !defined(UsingWizNet) && !defined(UsingEtherShield)
      while(true){
        digitalWrite(13,HIGH); delay(75);
        digitalWrite(13,LOW);  delay(75);
      }
#endif
    }

  // reset / reinitialize
  }else if(msg.checkString("~") || msg.checkString("init") || msg.checkString("reset")){
    reset();

  }
// status led
#if !defined(UsingWizNet) && !defined(UsingEtherShield)
  if(!(tmpStatusLedPattern & B10000000)){digitalWrite(13,LOW);}
#endif
}


// version
void ReadVersion(){
  eth_print("version: ");
  eth_print(VERSION);
#ifdef UsingWizNet
  eth_print(" WizNet");
#endif
#ifdef UsingEtherShield
  eth_print(" 28J60");
#endif
  eth_println();
}


// device name
void ReadName(){
  byte a;
  eth_print("name: ");
  for(byte t=MEMORY_NAME;t<255;t++){
    a=EEPROM.read(t);
    if(a<32 || a>126){break;}
    eth_print(a);
  } eth_println();
}
void WriteName(){
  byte tt=MEMORY_NAME;
  char a[255-MEMORY_NAME];
  msg.copyString(a,255-MEMORY_NAME);
  if(a[0]==0){return;}
  for(byte t=0;t<255-MEMORY_NAME;t++){
    if(tt==255){break;}
    if(a[t]<32 || a[t]>126){break;}
    EEPROM.write(tt,a[t]); tt++;
  } EEPROM.write(tt,0);
}


// pin mode
void SetMode(byte pin,byte mode){
  if(pin<PIN_START || pin>PIN_LAST){return;}
#if defined(UsingWizNet) || defined(UsingEtherShield)
  if(pin>=10 && pin<=13){return;}
#endif
  pin_modes[pin]=mode;
  // io output
  if(mode==MODE_IO){
    pinMode(pin,OUTPUT);
    SetPin(pin);
  // pwm
  }else if(mode==MODE_PWM){
    if(pin==3 || pin==5 || pin==6 || pin==9 || pin==10 || pin==11){
      pinMode(pin,OUTPUT);
      SetPin(pin);
    }else{
      pinMode(pin,INPUT); digitalWrite(pin,LOW);
      pin_modes[pin]=MODE_DISABLED;
      eth_println("invalid pwm pin");
    }
  // io input (floating)
  }else if(mode==MODE_IN){
    pinMode(pin,INPUT); digitalWrite(pin,LOW);
  // io input (pulled high)
  }else if(mode==MODE_INH){
    pinMode(pin,INPUT); digitalWrite(pin,HIGH);
  // analog input
  }else if(mode==MODE_ANALOG){
    pinMode(pin,INPUT); digitalWrite(pin,LOW);
    if(pin>=14 && pin<=19){
    }else{
      pin_modes[pin]=MODE_DISABLED;
      eth_println("invalid analog pin");
    }
  // disabled
  }else{
    pin_modes[pin]=MODE_DISABLED;
    pinMode(pin,INPUT); digitalWrite(pin,LOW);
  }
  EEPROM.write(MEMORY_MODES+pin,pin_modes[pin]);
}


// read mode
void ReadMode(byte pin){
  if(pin<PIN_START || pin>PIN_LAST){return;}
#if defined(UsingWizNet) || defined(UsingEtherShield)
  if(pin>=10 && pin<=13){return;}
#endif
  eth_print("mode "); eth_print(int(pin)); eth_print(' ');
  // io output
  if(pin_modes[pin]==MODE_IO){
    eth_print("io");
  // pwm
  }else if(pin_modes[pin]==MODE_PWM){
    eth_print("pwm");
  // io input (floating)
  }else if(pin_modes[pin]==MODE_IN){
    eth_print("in");
  // io input (pulled high)
  }else if(pin_modes[pin]==MODE_INH){
    eth_print("inh");
  // analog input
  }else if(pin_modes[pin]==MODE_ANALOG){
    eth_print("analog");
  // disabled
  }else{
    eth_print("x");
  } eth_println();
}


// set pin state
void SetPin(byte pin){
  if(pin<PIN_START || pin>PIN_LAST){return;}
#if defined(UsingWizNet) || defined(UsingEtherShield)
  if(pin>=10 && pin<=13){return;}
#endif
  // io
  if(pin_modes[pin]==MODE_IO){
    if(pin_states[pin]==0){digitalWrite(pin,LOW);}else{digitalWrite(pin,HIGH);}
  // pwm
  }else if(pin_modes[pin]==MODE_PWM){
    analogWrite(pin,pin_states[pin]);
  }
}


// read pin state
void ReadPin(byte pin){
  if(pin<PIN_START || pin>PIN_LAST){return;}
#if defined(UsingWizNet) || defined(UsingEtherShield)
  if(pin>=10 && pin<=13){return;}
#endif
  eth_print("pin "); eth_print(int(pin)); eth_print(' ');
  // io output
  if(pin_modes[pin]==MODE_IO){
    if(pin_states[pin]==0){eth_print("off");}else{eth_print("on");}
  // pwm
  }else if(pin_modes[pin]==MODE_PWM){
    eth_print(int(pin_states[pin]));
  // io input (floating)
  // io input (pulled high)
  }else if(pin_modes[pin]==MODE_IN || pin_modes[pin]==MODE_INH){
    if(digitalRead(pin)==HIGH){pin_states[pin]=1; eth_print("on");
    }else{                     pin_states[pin]=0; eth_print("off");}
  // analog input
  }else if(pin_modes[pin]==MODE_ANALOG){
    if(pin>=14 && pin<=PIN_LAST){
      eth_print(analogRead(pin-14));
    }else{
      eth_print("invalid analog pin");
    }
  }else{
    eth_print("x");
  } eth_println();
}

