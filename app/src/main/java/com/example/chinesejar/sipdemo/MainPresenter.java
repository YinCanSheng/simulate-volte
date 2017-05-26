package com.example.chinesejar.sipdemo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by chinesejar on 17-5-23.
 */

public class MainPresenter {

    private static final String TAG = "MainPresenter";
    private InetAddress inetAddress = null;
    private InetAddress address = null;
    private String package_data = null;
    private IMainView view;

    private String package_register =  "REGISTER sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
            "f: <sip:460027953821652@ims.mnc002.mcc460.3gppnetwork.org>;tag=1769466696\r\n" +
            "t: <sip:460027953821652@ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
            "CSeq: 695724863 REGISTER\r\n" +
            "i: 1769466687_2327919096@2409:8800:8a03:3f8f:ab28:5799:cd69:d1e8\r\n" +
            "v: SIP/2.0/TCP [2409:8800:8a03:3f8f:ab28:5799:cd69:d1e8]:8904;branch=z9hG4bK3700662738\r\n" +
            "Max-Forwards: 70\r\n" +
            "m: <sip:460027953821652@[2409:8800:8a03:3f8f:ab28:5799:cd69:d1e8]:8904>;+sip.instance=\"<urn:gsma:imei:86717902-002037-0>\";+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\";+g.3gpp.smsip;video\r\n" +
            "l: 0\r\n" +
            "Authorization: Digest uri=\"sip:ims.mnc002.mcc460.3gppnetwork.org\",username=\"460027953821652@ims.mnc002.mcc460.3gppnetwork.org\",response=\"\",realm=\"ims.mnc002.mcc460.3gppnetwork.org\",nonce=\"\"\r\n" +
            "Expires: 600000\r\n" +
            "Require: sec-agree\r\n" +
            "Proxy-Require: sec-agree\r\n" +
            "k: path,sec-agree\r\n" +
            "Allow: INVITE,BYE,CANCEL,ACK,NOTIFY,UPDATE,PRACK,INFO,MESSAGE,OPTIONS\r\n" +
            "Security-Client: ipsec-3gpp; alg=hmac-md5-96; ealg=des-ede3-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-md5-96; ealg=aes-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-md5-96; ealg=null; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-sha-1-96; ealg=des-ede3-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-sha-1-96; ealg=aes-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-sha-1-96; ealg=null; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904\r\n\r\n";
    private String package_invite = "INVITE tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
            "f: <tel:+8615008603839>;tag=1769475601\r\n" +
            "t: <tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
            "CSeq: 695733763 INVITE\r\n" +
            "i: 1769475587_2328286360@2409:8800:8a03:3f8f:ab28:5799:cd69:d1e8\r\n" +
            "v: SIP/2.0/TCP [2409:8800:8a03:3f8f:ab28:5799:cd69:d1e8]:8904;branch=z9hG4bK3750862093\r\n" +
            "Max-Forwards: 70\r\n" +
            "m: <sip:+8615008603839@[2409:8800:8a03:3f8f:ab28:5799:cd69:d1e8]:8904;user=phone>;+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\";video;+g.3gpp.mid-call;+g.3gpp.srvcc-alerting;+g.3gpp.ps2cs-srvcc-orig-pre-alerting\r\n" +
            "Route: <sip:[2409:8010:8210:1:1004:1004::]:9900;lr>,<sip:orig@ycscscf1bhw.nx.chinamobile.com;lr;Dpt=7c04_7f888246;ca=2385;TRC=ffffffff-ffffffff>\r\n" +
            "P-Access-Network-Info: 3GPP-E-UTRAN-TDD; utran-cell-id-3gpp=46000102D108F00D\r\n" +
            "Security-Verify: ipsec-3gpp;alg=hmac-md5-96;prot=esp;mod=trans;ealg=null;spi-c=2634663370;spi-s=3399226064;port-c=9950;port-s=9900\r\n" +
            "Proxy-Require: sec-agree\r\n" +
            "Require: sec-agree\r\n" +
            "P-Preferred-Identity: <tel:+8615008603839>\r\n" +
            "Allow: INVITE,ACK,CANCEL,BYE,UPDATE,PRACK,MESSAGE,REFER,NOTIFY,INFO\r\n" +
            "c: application/sdp\r\n" +
            "Accept: application/sdp,application/3gpp-ims+xml\r\n" +
            "P-Preferred-Service: urn:urn-7:3gpp-service.ims.icsi.mmtel\r\n" +
            "a: *;+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\"\r\n" +
            "k: 100rel,replaces,precondition,histinfo\r\n" +
            "P-Early-Media: supported\r\n" +
            "l: 655\r\n\r\n";
    private String package_option = "OPTIONS sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
            "Via: SIP/2.0/TCP [2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617]:8901;branch=z9hG4bK1519469541\r\n" +
            "Max-Forwards: 70\r\n" +
            "To: <tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
            "From: <tel:+8615008603839>;tag=1706771325\r\n" +
            "Call-ID: 1706771314_2327899832@2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617\r\n" +
            "CSeq: 633029490 OPTIONS\r\n" +
            "Contact: <sip:+8615008603839@[2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617]:8901;user=phone>;+g.3gpp.icsi-ref=\\\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\\\";video;+g.3gpp.mid-call;+g.3gpp.srvcc-alerting;+g.3gpp.ps2cs-srvcc-orig-pre-alerting\r\n" +
            "Accept: application/sdp\r\n" +
            "Content-Length: 0\r\n\r\n";
    private String package_refer = "REFER sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
            "Via: SIP/2.0/TCP [2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617]:8901;branch=z9hG4bK1519469541\r\n" +
            "Max-Forwards: 70\r\n" +
            "To: <tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
            "From: <tel:+8615008603839>;tag=1706771325\r\n" +
            "Call-ID: 1706771314_2327899832@2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617\r\n" +
            "CSeq: 633029490 OPTIONS\r\n" +
            "Contact: <sip:+8615008603839@[2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617]:8901;user=phone>;+g.3gpp.icsi-ref=\\\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\\\";video;+g.3gpp.mid-call;+g.3gpp.srvcc-alerting;+g.3gpp.ps2cs-srvcc-orig-pre-alerting\r\n" +
            "Accept: application/sdp\r\n" +
            "Content-Length: 0\r\n\r\n";

    public MainPresenter(IMainView view) {
        this.view = view;
    }

    public void setPackageData(String type, String data){
        switch (type){
            case "REGISTER":
                package_register = data;
            case "OPTION":
                package_option = data;
            case "INVITE":
                package_invite = data;
            case "REFER":
                package_refer = data;
        }
    }

    public List<String> getNetworkInterfaces(){
        List<String> network_interfaces = new ArrayList<String>();
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface iface : interfaces) {
                if (iface.isUp() && !iface.getDisplayName().equals("lo"))
                    network_interfaces.add(iface.getDisplayName());
            }
        } catch (SocketException e) {

        }
        network_interfaces.add("自定义");
        return network_interfaces;
    }

    public void socket_send(String tv_address, String displayName, String socket_type, String package_type){
        package_data = getPackageData(package_type);
        if (false) {
            view.sendFailed("请输入正确的 IPv6 地址");
        } else {
            try {
                address = Inet6Address.getByName(tv_address); //Inet6Address.getByName("2409:8010:8810:1:1003:1003::");

                if(displayName.equals("自定义")){
                    inetAddress = Inet6Address.getByName(view.getSrcIP());
                    if(socket_type.equals("TCP")){
                        new SendTCPSocketTask().execute();
                    }else if(socket_type.equals("UDP")){
                        new SendUDPSocketTask().execute();
                    }
                } else {
                    List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                    for (NetworkInterface iface : interfaces) {
                        if (iface.getDisplayName().equals(displayName)) {
                            Enumeration<InetAddress> nifAddresses = iface.getInetAddresses();

                            while (nifAddresses.hasMoreElements()) {
                                InetAddress ni = nifAddresses.nextElement();
                                if (ni.toString().substring(1, 5).equals("2409")) {
                                    Log.i(">>>>>>", ni.toString());
                                    inetAddress = ni;
                                    if (socket_type.equals("TCP")) {
                                        new SendTCPSocketTask().execute();
                                    } else if (socket_type.equals("UDP")) {
                                        new SendUDPSocketTask().execute();
                                    }

                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public String getPackageData(String package_type) {
        switch (package_type){
            case "REGISTER":
                return package_register;
            case "OPTION":
                return package_option;
            case "INVITE":
                return package_invite;
            case "REFER":
                return package_refer;
            default:
                return null;
        }
    }

    private class SendUDPSocketTask extends AsyncTask<String, Void, String> {
        private DatagramSocket socket;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            try {// 创建一个Socket对象，并指定服务端的IP及端口号
                socket = new DatagramSocket(new InetSocketAddress(inetAddress, 0));
                if (socket.isBound()) {
                    socket.connect(new InetSocketAddress(address, 5060));

                    DatagramPacket packet = new DatagramPacket(package_data.getBytes(), package_data.getBytes().length);

                    byte[] inBuff = new byte[4096];
                    // 以指定的字节数组创建准备接收数据的DatagramPacket对象
                    DatagramPacket inPacket = new DatagramPacket(inBuff , inBuff.length);

                    socket.setSoTimeout(5000);
                    socket.send(packet);
                    socket.receive(inPacket);
                    String rec_res = new String(inBuff, 0, inPacket.getLength());
                    Log.d("rec", rec_res);
                    return rec_res;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage() + "\n";
            }

            return "没有任何回复\n";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, result);
            view.sendSuccess(result);
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
        }
    }

    private class SendTCPSocketTask extends AsyncTask<String, Void, String> {
        private Socket socket;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";

            try {
                //客户端请求与本机在20006端口建立TCP连接
                socket = new Socket();
                socket.bind(new InetSocketAddress(inetAddress, 0));
                if (!socket.isBound()) {
                    socket.close();
                    return null;
                }
                socket.connect(new InetSocketAddress(address, 5060));
                socket.setSoTimeout(5000);
                //获取Socket的输入流，用来接收从服务端发送过来的数据
                BufferedReader buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //获取Socket的输出流，用来发送数据到服务端
                PrintStream out = new PrintStream(socket.getOutputStream());
                String str = package_data;
                //发送数据到服务端
                out.println(str);

                String reply=null;
                while(!((reply=buf.readLine())==null)){
                    res += reply+"\n";
                }

                if (socket != null) {
                    socket.shutdownOutput();
                    //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
                    socket.close(); //只关闭socket，其关联的输入输出流也会被关闭
                }
            } catch (IOException e) {
                return e.getMessage() + "\n";
            }
            return "没有任何回复\n";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, result);
            view.sendSuccess(result);
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
        }
    }
}
