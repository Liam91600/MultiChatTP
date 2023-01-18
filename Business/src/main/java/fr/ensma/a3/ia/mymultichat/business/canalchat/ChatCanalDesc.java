package fr.ensma.a3.ia.mymultichat.business.canalchat;
// TODO: Documentation

/**
 * Modélisation d'un canal de Chat
 */
public class ChatCanalDesc {

    private Integer canalId;
    private String canalName;
    private String canalDescription;

    public ChatCanalDesc() {
    }

    public ChatCanalDesc(final int cid, final String name) {
        canalId = cid;
        canalName = name;
    }

    public ChatCanalDesc(final int cid, final String name, final String desc) {
        canalId = cid;
        canalName = name;
        canalDescription = desc;
    }

    public final Integer getCanalId() {
        return canalId;
    }

    public final void setCanalId(final Integer cid) {
        canalId = cid;
    }

    public final String getcanalName() {
        return canalName;
    }

    public final void setCanalName(final String name) {
        canalName = name;
    }

    public final String getCanalDescription() {
        return canalDescription;
    }

    public final void setCanalDescription(final String desc) {
        canalDescription = desc;
    }

    @Override
    public String toString() {
        return canalId + " : -> " + canalName + " : " + canalDescription;
    }
}
