import java.util.Scanner;

public class Servidor50 {
   TCPServer50 mTcpServer;
   Scanner sc;
   public static void main(String[] args) {
       Servidor50 objser = new Servidor50();
       objser.iniciar();
   }
   void iniciar(){
       new Thread(
            new Runnable() {

                @Override
                public void run() {
                      mTcpServer = new TCPServer50(
                        new TCPServer50.OnMessageReceived(){
                            @Override
                            public void messageReceived(String message){
                                ServidorRecibe(message);
                            }
                        }
                    );
                    mTcpServer.run();                   
                }
            }
        ).start();
        //-----------------
        String salir = "n";
        sc = new Scanner(System.in);
        System.out.println("Servidor bandera 01");
        while( !salir.equals("s")){
            salir = sc.nextLine();
            ServidorEnvia(salir);
       }
       System.out.println("Servidor bandera 02"); 
   
   }
   void ServidorRecibe(String llego){
       String respuesta;
       if (llego.matches("^envia \\d+$")) {
           int numero = Integer.parseInt(llego.substring(6).trim());
           respuesta = "Respuesta: " + (numero + 10);
       } else {
           respuesta = "Formato incorrecto: use 'envia <numero>'";
       }
       System.out.println("SERVIDOR40 El mensaje:" + llego);
       if (mTcpServer != null) {
           mTcpServer.sendMessageTCPServer(respuesta);
       }
   }
   void ServidorEnvia(String envia){
        // Solo acepta el formato exacto "envia <numero>"
        String respuesta;
        if (envia.matches("^envia \\d+$")) {
            int numero = Integer.parseInt(envia.substring(6).trim());
            respuesta = "Respuesta: " + (numero + 10);
        } else {
            respuesta = "Formato incorrecto: use 'envia <numero>'";
        }
        if (mTcpServer != null) {
            mTcpServer.sendMessageTCPServer(respuesta);
        }
   }
}
