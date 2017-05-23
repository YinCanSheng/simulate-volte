package com.example.chinesejar.sipdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private List<String> networks = new ArrayList<String>();

    private InetAddress address = null;
    private Integer port = 5060;
    private InetAddress inet = null;

    private Button btn_send = null;
    private TextView tv_networks = null;
    private Spinner spinner_networks = null;
    private EditText et_dstip = null;
    private ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_send = (Button) findViewById(R.id.btn_send);
        tv_networks = (TextView) findViewById(R.id.tv_networks);
        spinner_networks = (Spinner) findViewById(R.id.spinner_networks);
        et_dstip = (EditText) findViewById(R.id.et_dstip);

        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface iface : interfaces) {
                if (iface.isUp() && !iface.getDisplayName().equals("lo"))
                    networks.add(iface.getDisplayName());
            }
        } catch (SocketException e) {

        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, networks.toArray(new String[networks.size()]));
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        spinner_networks.setAdapter(adapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (false) {
                    Toast.makeText(MainActivity.this, "请输入正确的 IPv6 地址", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        address = Inet6Address.getByName(et_dstip.getText().toString()); //Inet6Address.getByName("2409:8010:8810:1:1003:1003::");

                        List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                        for (NetworkInterface iface : interfaces) {
                            if (iface.getDisplayName().equals(spinner_networks.getSelectedItem().toString())) {
                                Enumeration<InetAddress> nifAddresses = iface.getInetAddresses();

                                while (nifAddresses.hasMoreElements()) {
                                    InetAddress ni = nifAddresses.nextElement();
                                    if (ni.toString().substring(1, 5).equals("2409")) {
                                        Log.i(">>>>>>", ni.toString());
                                        inet = ni;

                                        // new SendUDPSocketTask().execute();
                                        new SendUDPSocketTask().execute();
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        Log.e("err", e.getMessage());
                    }
                }
            }
        });
    }

    public boolean isIPv6(String str) {
        if (!Pattern.matches("[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][:][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][:][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][:][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][:][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][:][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][:][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][:][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]", str))
            return false;
        return true;
    }

    private class SendUDPSocketTask extends AsyncTask<String, Void, String> {
        private DatagramSocket socket;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {// 创建一个Socket对象，并指定服务端的IP及端口号
                socket = new DatagramSocket(new InetSocketAddress(inet, 0));
                if (socket.isBound()) {
                    socket.connect(new InetSocketAddress(address, 5060));

                    String option = "OPTIONS sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
                            "Via: SIP/2.0/TCP [2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617]:8901;branch=z9hG4bK1519469541\r\n" +
                            "Max-Forwards: 70\r\n" +
                            "To: <tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
                            "From: <tel:+8615008603839>;tag=1706771325\r\n" +
                            "Call-ID: 1706771314_2327899832@2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617\r\n" +
                            "CSeq: 633029490 OPTIONS\r\n" +
                            "Contact: <sip:+8615008603839@[2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617]:8901;user=phone>;+g.3gpp.icsi-ref=\\\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\\\";video;+g.3gpp.mid-call;+g.3gpp.srvcc-alerting;+g.3gpp.ps2cs-srvcc-orig-pre-alerting\r\n" +
                            "Accept: application/sdp\r\n" +
                            "Content-Length: 0\r\n\r\n";

                    String invite = "INVITE tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
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
                    String message = "REGISTER sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
                            "f: <sip:460027953821652ddd@ims.mnc002.mcc460.3gppnetwork.org>;tag=1769466696\r\n" +
                            "t: <sip:460027953821652@ims.mnc000.mcc460.3gppnetwork.org>\r\n" +
                            "CSeq: 695724864 REGISTER\r\n" +
                            "i: 1769466687_2327919096@2409:8800:8a03:3f8f:ab28:5799:cd69:d1e8\r\n" +
                            "v: SIP/2.0/UDP [2409:8800:8a03:3f8f:ab28:5799:cd69:d1e8]:5060;branch=z9hG4bK3700662738\r\n" +
                            "Max-Forwards: 70\r\n" +
                            "m: <sip:460027953821652@[2409:8800:8a03:3f8f:ab28:5799:cd69:d1e8]:5060>;+sip.instance=\"<urn:gsma:imei:86717902-002037-0>\";+g.3gpp.icsi-ref=\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\";+g.3gpp.smsip;video\r\n" +
                            "Authorization: Digest uri=\"sip:ims.mnc002.mcc460.3gppnetwork.org\",username=\"460027953821652@ims.mnc002.mcc460.3gppnetwork.org\",response=\"\",realm=\"ims.mnc002.mcc460.3gppnetwork.org\",nonce=\"\"\r\n" +
                            "Expires: 600000\r\n" +
                            "Require: sec-agree\r\n" +
                            "Proxy-Require: sec-agree\r\n" +
                            "k: path,sec-agree\r\n" +
                            "Allow: INVITE,BYE,CANCEL,ACK,NOTIFY,UPDATE,PRACK,INFO,MESSAGE,OPTIONS\r\n" +
                            "Security-Client: ipsec-3gpp; alg=hmac-md5-96; ealg=des-ede3-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-md5-96; ealg=aes-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-md5-96; ealg=null; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-sha-1-96; ealg=des-ede3-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-sha-1-96; ealg=aes-cbc; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904,ipsec-3gpp; alg=hmac-sha-1-96; ealg=null; spi-c=1028536224; spi-s=455113919; port-c=8034; port-s=8904\r\n" +
                            "l: 0\r\n\r\n";
                    DatagramPacket packet = new DatagramPacket(invite.getBytes(), invite.getBytes().length);
                    socket.setSoTimeout(10000);
                    socket.send(packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                byte[] recbuf = new byte[255];
                DatagramPacket recpacket = new DatagramPacket(recbuf,
                        recbuf.length);
                try {
                    socket.setSoTimeout(3000);
                    socket.receive(recpacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String recdata = recpacket.getData().toString();
                Log.d("rec", "Server: Message received: " + recdata);
                Log.d("rec", "Server: IP " + recpacket.getAddress());
            }
        }

        @Override
        protected void onPostExecute(String result) {
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

            String option = "OPTIONS sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
                    "Via: SIP/2.0/TCP [2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617]:8901;branch=z9hG4bK1519469541\r\n" +
                    "Max-Forwards: 70\r\n" +
                    "To: <tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org>\r\n" +
                    "From: <tel:+8615008603839>;tag=1706771325\r\n" +
                    "Call-ID: 1706771314_2327899832@2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617\r\n" +
                    "CSeq: 633029490 OPTIONS\r\n" +
                    "Contact: <sip:+8615008603839@[2409:8800:8a03:3f8f:e19b:3c22:ddb1:5617]:8901;user=phone>;+g.3gpp.icsi-ref=\\\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\\\";video;+g.3gpp.mid-call;+g.3gpp.srvcc-alerting;+g.3gpp.ps2cs-srvcc-orig-pre-alerting\r\n" +
                    "Accept: application/sdp\r\n" +
                    "Content-Length: 0\r\n\r\n";

            String invite = "INVITE tel:18210173588;phone-context=ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
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

            String message = "REGISTER sip:ims.mnc002.mcc460.3gppnetwork.org SIP/2.0\r\n" +
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


            try {
                //客户端请求与本机在20006端口建立TCP连接
                socket = new Socket();
                socket.bind(new InetSocketAddress(inet, 0));
                if (!socket.isBound()) {
                    socket.close();
                    return null;
                }
                socket.connect(new InetSocketAddress(address, 5060));
                socket.setSoTimeout(10000);
                //获取Socket的输入流，用来接收从服务端发送过来的数据
                BufferedReader buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //获取Socket的输出流，用来发送数据到服务端
                PrintStream out = new PrintStream(socket.getOutputStream());
                boolean flag = true;
                while (flag) {
                    String str = option;
                    //发送数据到服务端
                    out.println(str);
                    if ("bye".equals(str)) {
                        flag = false;
                    } else {
                        try {
                            //从服务器端接收数据有个时间限制（系统自设，也可以自己设置），超过了这个时间，便会抛出该异常
                            String echo = buf.readLine();
                            Log.d("res", echo);
                        } catch (SocketTimeoutException e) {
                            Log.d("res", "Time out, No response");
                        }
                    }
                }
                if (socket != null) {
                    //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
                    socket.close(); //只关闭socket，其关联的输入输出流也会被关闭
                }
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
        }
    }
}
