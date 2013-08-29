/*
  EthernetLayer.pde - Compatibility layer for ethernet shields
  Created by Matthew Kemmerer, Jan 1, 2012
  Released into the public domain.
*/


// only one define
#ifdef UsingWizNet
#undef UsingEtherShield
#endif

// include wiz ethernet
#ifdef UsingWizNet
#include <SPI.h>
#include <Ethernet.h>
#endif

// include etherShield
#ifdef UsingEtherShield
#define TCP_START_P TCP_CHECKSUM_L_P+3
#include <EtherShield.h>
#endif

// buffer sizes
#define SERIAL_IN_BUF_SIZE  250
#define ETH_IN_BUF_SIZE     250
#define ETH_OUT_BUF_SIZE    400

// buffers
static uint8_t serial_in_buf[SERIAL_IN_BUF_SIZE+1];
static unsigned int serial_in_p,serial_in_c; // buffer string length
#if defined(UsingWizNet) || defined(UsingEtherShield)
static uint8_t eth_in_buf[ETH_IN_BUF_SIZE+1];
static uint8_t eth_out_buf[ETH_OUT_BUF_SIZE+1];
static unsigned int eth_in_p,eth_out_p; // buffer string length
static unsigned int eth_in_c,eth_out_c; // buffer read/write position
#endif

// wiznet class
#ifdef UsingWizNet
Server server(eth_port);
Client client(MAX_SOCK_NUM);
#endif
// ethershield class
#ifdef UsingEtherShield
EtherShield es=EtherShield();
#endif


// ethernet setup
void ethLayer_setup(){
  serial_in_p=0; serial_in_c=0;
// setup wiz ethernet
#ifdef UsingWizNet
  eth_in_p=0; eth_out_p=0;
  eth_in_c=0; eth_out_c=0;
  Ethernet.begin(eth_mac,eth_ip,eth_gateway,eth_subnet);
  server.begin();
#endif
// setup etherShield
#ifdef UsingEtherShield
  eth_in_p=0; eth_out_p=0;
  eth_in_c=0; eth_out_c=0;
  es.ES_enc28j60SpiInit();
  es.ES_enc28j60Init(eth_mac);
  es.ES_init_ip_arp_udp_tcp(eth_mac,eth_ip,eth_port);
  if(es.ES_enc28j60Revision()<=0){
    Serial.println("Failed to access ENC28J60");
  }
#endif
}


// ethernet loop
void ethLayer_loop(){
#if defined(UsingWizNet) || defined(UsingEtherShield)
  eth_out_p=0; eth_out_c=0;
#endif
// serial loop
#ifdef ethPrintToSerial
  while(Serial.available()){
    serial_in_buf[serial_in_p]=Serial.read();
    if(serial_in_buf[serial_in_p]=='\r'){
      Serial.println();
      serial_in_p++;
      GotData();
    }else{
      serial_in_p++;
    }
  }
#endif
// wiznet loop
#ifdef UsingWizNet
  client=server.available();
  if(client){GotData();}
#endif
// eithershield loop
#ifdef UsingEtherShield
  eth_in_buf[0]='\0'; eth_out_buf[0]='\0';
  // get packet
  eth_in_p=es.ES_enc28j60PacketReceive(ETH_IN_BUF_SIZE,eth_in_buf);
  if(eth_in_p==0){return;}
  // process packet
  eth_in_c=es.ES_packetloop_icmp_tcp(eth_in_buf,eth_in_p);
  if(eth_in_c==0){return;}
  // copy in buffer to out buffer
  // (to retain tcp header)
  for(unsigned int t=0;t<TCP_START_P;t++){
    eth_out_buf[t]=eth_in_buf[t];
  } GotData();
#endif
}


// data available
boolean ethLayer_available(){
// serial available
  if(serial_in_p!=0){return(serial_in_c!=serial_in_p);}
// wiznet available
#ifdef UsingWizNet
  if(client){return(client.available());}else{return(false);}
#endif
// ethershield available
#ifdef UsingEtherShield
  if(eth_in_c==0){return(false);}
  if(eth_in_c>=ETH_IN_BUF_SIZE || eth_in_buf[eth_in_c]==0){eth_in_c=0;}
  return(eth_in_c!=0);
#endif
}


// read data
char ethLayer_read(){
// read serial
  if(serial_in_p!=0){serial_in_c++; return(serial_in_buf[serial_in_c-1]);}
// read wiznet
#ifdef UsingWizNet
  return(client.read());
#endif
// read ethershield
#ifdef UsingEtherShield
  if(eth_in_c==0){return('\0');}
  eth_in_c++; return(eth_in_buf[eth_in_c-1]);
#endif
}


// clear buffer
void ethLayer_clear(){
  serial_in_c=0; serial_in_p=0;
#ifdef UsingWizNet
  eth_in_c=0; eth_in_p=0;
#endif
#ifdef UsingEtherShield
  eth_in_c=0; eth_in_p=0;
  // clear buffer
  for(unsigned int t=0;t<ETH_IN_BUF_SIZE;t++){eth_in_buf[t]=0;}
#endif
}


// send tcp data / close connection
void ethLayer_send(){
#ifdef UsingWizNet
  for(unsigned int t=0;t<eth_out_c;t++){
    client.write(eth_out_buf[t]);
  } client.stop();
#endif
#ifdef UsingEtherShield
  es.ES_www_server_reply(eth_out_buf,eth_out_c);
#endif
}


// print functions
void eth_write(int b){
#ifdef ethPrintToSerial
  if(b!='\r'){if(b=='\n'){Serial.println();}else{Serial.write(b);}}
#endif
#ifdef UsingWizNet
  if(eth_out_c>=ETH_OUT_BUF_SIZE){return;}
  eth_out_buf[eth_out_c]=b;
  eth_out_c++;
#endif
#ifdef UsingEtherShield
  if(eth_out_c+TCP_START_P>=ETH_OUT_BUF_SIZE){return;}
  eth_out_buf[eth_out_c+TCP_START_P]=b;
  eth_out_c++;
#endif
}

void eth_write  (const char *str){while(*str){eth_write(*str++);}}
void eth_write  (const uint8_t *buffer,size_t size){while(size--){eth_write(*buffer++);}}
void eth_println(void){eth_print('\r'); eth_print('\n');}
// string
void eth_print  (const String &s){for(int i=0;i<s.length();i++){eth_write(s[i]);}}
void eth_println(const String &s){eth_print(s); eth_println();}
// char[]
void eth_print  (const char str[]){eth_write(str);}
void eth_println(const char c[]){  eth_print(c); eth_println();}
// char
void eth_print  (char c,int base){eth_print((long)c,base);}
void eth_println(char c,int base){eth_print(c,base); eth_println();}
// unsigned char
void eth_print  (unsigned char b,int base){eth_print((unsigned long)b,base);}
void eth_println(unsigned char b,int base){eth_print(b,base); eth_println();}
// int
void eth_print  (int n,int base){eth_print((long)n,base);}
void eth_println(int n,int base){eth_print(n,base); eth_println();}
// unsigned int
void eth_print  (unsigned int n,int base){eth_print((unsigned long)n,base);}
void eth_println(unsigned int n,int base){eth_print(n,base); eth_println();}
// long
void eth_println(long n,int base){eth_print(n,base); eth_println();}
void eth_print  (long n,int base){
  if(base==0){       eth_write(n);
  }else if(base==10){if(n<0){eth_print('-'); n=-n;} eth_printNumber(n,10);
  }else{             eth_printNumber(n,base);}
}
// unsigned long
void eth_print  (unsigned long n,int base){if(base==0){eth_write(n);}else{eth_printNumber(n,base);}}
void eth_println(unsigned long n,int base){eth_print(n,base); eth_println();}
// double
void eth_print  (double n,int digits){eth_printFloat(n,digits);}
void eth_println(double n,int digits){eth_print(n,digits); eth_println();}

void eth_printNumber(unsigned long n,uint8_t base){
  unsigned char buf[8 * sizeof(long)]; // Assumes 8-bit chars
  unsigned long i=0;
  if(n==0){  eth_print('0'); return;}
  while(n>0){buf[i++]=n%base; n/=base;}
  for(;i>0;i--){eth_print((char)(buf[i-1]<10?'0'+buf[i-1]:'A'+buf[i-1]-10));}
}

void eth_printFloat(double number,uint8_t digits){ 
  // Handle negative numbers
  if(number<0.0){eth_print('-'); number=-number;}
  // Round correctly so that print(1.999, 2) prints as "2.00"
  double rounding=0.5;
  for(uint8_t i=0;i<digits;++i){rounding/=10.0;}
  number+=rounding;
  // Extract the integer part of the number and print it
  unsigned long int_part=(unsigned long)number;
  double remainder=number-(double)int_part;
  // Print the decimal point, but only if there are digits beyond
  eth_print(int_part); if(digits>0){eth_print('.');}
  // Extract digits from the remainder one at a time
  while(digits-->0){
    remainder*=10.0; int toPrint=int(remainder);
    eth_print(toPrint);  remainder-=toPrint;
  }
}
