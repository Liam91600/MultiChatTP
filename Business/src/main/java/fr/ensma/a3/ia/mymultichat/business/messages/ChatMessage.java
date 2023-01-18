package fr.ensma.a3.ia.mymultichat.business.messages;

/**
 * Message échangé par les clients.
 *
 * @author Mikky Richard
 */
public class ChatMessage {

    private int canalId;
    private String lePseudo;
    private String leContenu;

    public final int getCanalId() {
        return canalId;
    }

    public final void setCanalId(final int cid) {
        canalId = cid;
    }

    public final String getLePseudo() {
        return lePseudo;
    }

    public final void setLePseudo(final String pseu) {
        lePseudo = pseu;
    }

    public final String getLeContenu() {
        return leContenu;
    }

    public final void setLeContenu(final String m) {
        leContenu = m;
    }

}
