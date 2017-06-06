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
import java.net.UnknownHostException;
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
    private String packageData = null;
    private IMainView view;

    private String imsi = null;

    private String packageRegister = null;

    public String getPackageRegister() {
        String imsi = view.getIMSI();
        InetAddress ia = getInetAddress();
        if(imsi != null && ia != null) {
            String address = ia.getHostAddress();
            return "REGISTER sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
                    "f: <sip:" + imsi + "@ims.mnc002.mcc460.3gppnetwork.org>;tag=1769467592\r\n" +
                    "t: <sip:" + imsi + "@ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
                    "CSeq: 695724864 REGISTER\r\n" +
                    "i: 1769466687_2327919096@" + address + "\r\n" +
                    "v: SIP/2.0/TCP [" + address + "]:8904;branch=z9hG4bK3400948792\r\n" +
                    "Max-Forwards: 70\r\n" +
                    "m: <sip:" + imsi + "@[" + address + "]:8904>;+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\";+g.3gpp.smsip;video;+sip.instance=\"<urn:gsma:imei:86717902-002037-0>\"\r\n" +
                    "P-Access-Network-Info: 3GPP-E-UTRAN-TDD; utran-cell-id-3gpp=46000102D108F00D\r\n" +
                    "Security-Verify: ipsec-3gpp;alg=hmac-md5-96;prot=esp;mod=trans;ealg=null;spi-c=2634663370;spi-s=3399226064;port-c=9950;port-s=9900\r\n" +
                    "l: 0\r\n" +
                    "Authorization: Digest username=\"460027953821652@ims.mnc002.mcc460.3gppnetwork.org\",realm=\"ims.mnc002.mcc460.3gppnetwork.org\",uri=\"sip:ims.mnc002.mcc460.3gppnetwork.org\",nonce=\"SVLOv+/iIqtM7mwk9MoGxLZz0aj5V3JMT5MM5VPmcdY=\",algorithm=AKAv1-MD5,response=\"6adb27c78ae901dd986cd61eb8de0b94\"\r\n" +
                    "Expires: 600000\r\n" +
                    "Require: sec-agree\r\n" +
                    "Proxy-Require: sec-agree\r\n" +
                    "k: path,sec-agree\r\n" +
                    "Allow: INVITE,BYE,CANCEL,ACK,NOTIFY,UPDATE,PRACK,INFO,MESSAGE,OPTIONS\r\n" +
                    "Security-Client: ipsec-3gpp; alg=hmac-md5-96; ealg=des-ede3-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-md5-96; ealg=aes-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-md5-96; ealg=null; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-sha-1-96; ealg=des-ede3-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-sha-1-96; ealg=aes-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-sha-1-96; ealg=null; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904\r\n" +
                    "\r\n\r\n";
        }
        return "";
    }
    private String packageInvite = null;

    public String getPackageInvite() {
        String dstAddress = view.getDstIP();
        InetAddress ia = getInetAddress();
        if(dstAddress != null && ia != null) {
            String address = ia.getHostAddress();
            return "INVITE tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
                    "f: <tel:+8615008603839>;tag=1769475601\r\n" +
                    "t: <tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
                    "CSeq: 695733763 INVITE\r\n" +
                    "i: 1769475587_2328286360@" + address + "\r\n" +
                    "v: SIP/2.0/TCP [" + address + "]:8904;branch=z9hG4bK3750862093\r\n" +
                    "Max-Forwards: 70\r\n" +
                    "m: <sip:+8615008603839@[" + address + "]:8904;user=phone>;+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\";video;+g.3gpp.mid-call;+g.3gpp.srvcc-alerting;+g.3gpp.ps2cs-srvcc-orig-pre-alerting\r\n" +
                    "Route: <sip:[" + dstAddress + "]:9900;lr>,<sip:orig@ycscscf1bhw.nx.chinamobile.com;lr;Dpt=7c04_7f888246;ca=2385;TRC=ffffffff-ffffffff>\r\n" +
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
        }
        return "";
    }
    private String packageOption = null;

    public String getPackageOption() {
        InetAddress ia = getInetAddress();
        if(ia != null) {
            String address = ia.getHostAddress();
            return "OPTIONS sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
                    "Via: SIP/2.0/TCP [" + address + "]:8901;branch=z9hG4bK1519469541\r\n" +
                    "Max-Forwards: 70\r\n" +
                    "To: <tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
                    "From: <tel:+8615008603839>;tag=1706771325\r\n" +
                    "Call-ID: 1706771314_2327899832@" + address + "\r\n" +
                    "CSeq: 633029490 OPTIONS\r\n" +
                    "Contact: <sip:+8615008603839@[" + address + "]:8901;user=phone>;+g.3gpp.icsi-ref=\\\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\\\";video;+g.3gpp.mid-call;+g.3gpp.srvcc-alerting;+g.3gpp.ps2cs-srvcc-orig-pre-alerting\r\n" +
                    "Accept: application/sdp\r\n" +
                    "Content-Length: 0\r\n\r\n";
        }
        return "";
    }
    private String packageRefer = null;

    public String getPackageRefer() {
        InetAddress ia = getInetAddress();
        if(ia != null) {
            String address = ia.getHostAddress();
            return "REFER sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
                    "Via: SIP/2.0/TCP [" + address + "]:8901;branch=z9hG4bK1519469541\r\n" +
                    "Max-Forwards: 70\r\n" +
                    "To: <tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
                    "From: <tel:+8615008603839>;tag=1706771325\r\n" +
                    "Call-ID: 1706771314_2327899832@" + address + "\r\n" +
                    "CSeq: 633029490 OPTIONS\r\n" +
                    "Contact: <sip:+8615008603839@[" + address + "]:8901;user=phone>;+g.3gpp.icsi-ref=\\\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\\\";video;+g.3gpp.mid-call;+g.3gpp.srvcc-alerting;+g.3gpp.ps2cs-srvcc-orig-pre-alerting\r\n" +
                    "Accept: application/sdp\r\n" +
                    "Content-Length: 0\r\n\r\n";
        }
        return "";
    }

    public MainPresenter(IMainView view) {
        this.view = view;
    }

    public void setPackageData(String type, String data){
        switch (type){
            case "REGISTER":
                packageRegister = data;
            case "OPTION":
                packageOption = data;
            case "INVITE":
                packageInvite = data;
            case "REFER":
                packageRefer = data;
        }
    }

    public List<String> getNetworkInterfaces(){
        List<String> networkInterfaces = new ArrayList<String>();
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface iface : interfaces) {
                if (iface.isUp() && !iface.getDisplayName().equals("lo"))
                    networkInterfaces.add(iface.getDisplayName());
            }
        } catch (SocketException e) {

        }
        networkInterfaces.add("自定义");
        return networkInterfaces;
    }

    // 获取 IPv6 地址
    public InetAddress getInetAddress(){
        String displayName = view.getNetworkInterface();
        try{
            if(displayName.equals("自定义")){
                String srcAddress = view.getSrcIP();
                if (srcAddress == null || srcAddress.length() == 0){
                    return null;
                }
                return Inet6Address.getByName(srcAddress);
            } else {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface iface : interfaces) {
                    if (iface.getDisplayName().equals(displayName)) {
                        Enumeration<InetAddress> nifAddresses = iface.getInetAddresses();

                        while (nifAddresses.hasMoreElements()) {
                            InetAddress ni = nifAddresses.nextElement();
                            if (ni.toString().substring(1, 5).equals("2409")) {
                                Log.i(">>>>>>", ni.toString());
                                return ni;
                            }
                        }
                    }
                }
            }
        }
        catch (UnknownHostException e){
            Log.e(TAG, e.getMessage());
        }catch (SocketException e){
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public void socketSend(String tvAddress, String socketType, String packageType){
        packageData = getPackageData(packageType);
        if (tvAddress == null || tvAddress.length() == 0) {
            view.sendFailed("请输入正确的目标 IPv6 地址");
        } else {
            inetAddress = getInetAddress();
            if (inetAddress != null){
                try {
                    address = Inet6Address.getByName(tvAddress); //Inet6Address.getByName("2409:8010:8810:1:1003:1003::");
                    if (socketType.equals("TCP")) {
                        new SendTCPSocketTask().execute();
                    } else if (socketType.equals("UDP")) {
                        new SendUDPSocketTask().execute();
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    public String getPackageData(String package_type) {
        switch (package_type){
            case "REGISTER":
                return getPackageRegister();
            case "OPTION":
                return getPackageOption();
            case "INVITE":
                return getPackageInvite();
            case "REFER":
                return getPackageRefer();
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

                    DatagramPacket packet = new DatagramPacket(packageData.getBytes(), packageData.getBytes().length);

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
                String str = packageData;
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
