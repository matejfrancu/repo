package vyjimky;

import java.io.File;

/**
 *  Vyjímka které je vyhozena v případě že program nemůže smazat soubor
 * @author Matěj Franců
 * 
 */
public class NelzeSmazat extends RuntimeException{
    public NelzeSmazat(File x){
        super(String.format("Deleting falied %s", x.getAbsolutePath()));
    }
}
