package fr.ensma.a3.ia.mymultichat.business.canalchat;
// TODO: Documentation

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

/**
 * GestionCanaux
 */
public class GestionCanaux {

    private List<ChatCanalDesc> listCanaux;

    public GestionCanaux() {
        listCanaux = new ArrayList<ChatCanalDesc>();
        // FIXME: Ajout couche DAL, faire le lien genre apeller le DAO etc, ici pour l'exemple on rajoute le canal a la main
        listCanaux.add(new ChatCanalDesc(1, "MikkyChat", "Canal de chat privé de Mikky..."));
        listCanaux.add(new ChatCanalDesc(2, "LiamChat", "Canal de chat privé de Liam..."));
        listCanaux.add(new ChatCanalDesc(5, "AudranChat", "Canal de chat privé de Audran..."));

    }

    public Optional<ChatCanalDesc> getCanal(final int idcan) {
        ListIterator<ChatCanalDesc> ite = listCanaux.listIterator();
        boolean fin = false;
        Optional<ChatCanalDesc> res = null;
        ChatCanalDesc elem = null;
        while (ite.hasNext() && !fin) {
            elem = ite.next();
            if (elem.getCanalId().equals(idcan)) {
                fin = true;
                res = Optional.of(elem);
            }
        }
        if (elem == null) {
            res = Optional.empty();
        }
        return res;
    }
    
    public List<ChatCanalDesc> getListCanal(){
    	return listCanaux;
    }

}
