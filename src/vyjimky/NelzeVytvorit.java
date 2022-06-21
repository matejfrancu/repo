package vyjimky;

import java.io.File;

/**
 * Vyjímka které je vyhozena v případě že program nemůže vytvořit soubor
 * @author Matěj Franců
 * 
 */
public class NelzeVytvorit extends RuntimeException{
    public NelzeVytvorit(File x){
        super(String.format("Creating falied %s", x.getAbsolutePath()));
    }
}
