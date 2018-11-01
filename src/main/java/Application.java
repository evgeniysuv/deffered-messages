public class Application {
    public static void main(String[] args) {
        Receiver reciever = new ReceiverImpl();
        MessagePusher pusher = new MessagePusher(5, reciever);
        pusher.generateMessages();
    }
}
