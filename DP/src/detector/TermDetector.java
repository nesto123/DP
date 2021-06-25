package detector;

import message.Msg;

public interface TermDetector {
    public void initiate();
    public void sendAction();
    public void turnPassive();
    public void handleMsg( Msg m, int srcsId, String tag );
    public boolean isDone();
    public void setFinished();
}
