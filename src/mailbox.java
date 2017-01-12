import java.util.List;

public class mailbox {
    int mailboxID;
    List<msg> msg;
    List<String> member;
    public class msg{
        String user;
        String content;
    }
}
