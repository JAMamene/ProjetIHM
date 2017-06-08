package ihm.projetihm.Model;

import java.util.Arrays;
import java.util.List;

import static ihm.projetihm.Model.Category.ACCESSOIRES;
import static ihm.projetihm.Model.Category.DECO;
import static ihm.projetihm.Model.Category.*;
import static ihm.projetihm.Model.Category.MAROQUINERIE;

public enum Source {
    TWITTER(1),
    MAGASIN(2),
    EVENEMENT(3),
    ALL(4);

    private int id;

    Source(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static final List<Category> MAGASIN_CATEGORIES = Arrays.asList(
            MAROQUINERIE,
            ACCESSOIRES,
            DECO,
            JEUXVIDEO
    );

    public static final List<Category> EVENEMENT_CATEGORIES = Arrays.asList(
            STAGE,
            CONCOURS,
            JEU
    );
    public static final List<Category> COMMON_CATEGORIES = Arrays.asList(
            MODE,
            ALIMENTATION,
            INFORMATION,
            SANTE,
            MEME,
            TECHNOLOGIE
    );
}