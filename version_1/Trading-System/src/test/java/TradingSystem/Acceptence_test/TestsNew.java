package TradingSystem.Acceptence_test;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.TradingSystemApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = TradingSystemApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestNew {

    @LocalServerPort
    private int port;

    private SockJsClient sockJsClient;

    private WebSocketStompClient stompClient;

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    Client client;

    @BeforeEach
    public void setup() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.sockJsClient = new SockJsClient(transports);

        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.client = new Client();
        client.clearSystem();
        client.connectSystemNew(port);
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }


    @Test
    public void Register() throws Exception {


        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();


        StompSessionHandler handler = new TestSessionHandler(failure) {

            @Override
            public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {

                session.subscribe(String.format("/topic/%s", client.getConnID()), new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Response.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Response response = (Response) payload;
                        try {
                            System.out.println(response.getMessage());
                            assertFalse(response.getIsErr());
                        } catch (Throwable t) {
                            failure.set(t);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }
                    }
                });
                try {
                  //  client=new Client();
              //      client.connectSystem();
                    Map<String, String> obj= new HashMap<>();
                    obj.put("connID", client.getConnID());
                    obj.put("userName", "reutlevy");
                    obj.put("password", "811996");
                    session.send("/app/register",obj);
                } catch (Throwable t) {
                    failure.set(t);
                    latch.countDown();
                }
            }
        };


        this.stompClient.connect("ws://localhost:{port}/ws-message", this.headers, handler, this.port);
        if (latch.await(5, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        }
        else {
            fail("Unable to register");
        }

    }

    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;

        public TestSessionHandler(AtomicReference<Throwable> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(ex);
        }
    }
}
