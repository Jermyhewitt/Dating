package snaplic.com.dating;

/**
 * Created by Jermaine on 26/7/2016.

 */
public class MessageObject {
    private  String sender,destination,content,timestamp;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    MessageObject()
    {

    }

    MessageObject(String sent,String recv,String data)
    {
        sender=sent;
        destination=recv;
        content=data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String toString()
    {
        return ("content : "+content+" sender: "+sender+ "receiver: "+destination);
    }
}
