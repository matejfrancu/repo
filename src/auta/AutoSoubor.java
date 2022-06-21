package auta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import vyjimky.NelzeVytvorit;

/**
 *
 * @author Matěj Franců
 */
public class AutoSoubor {
    
    /**
     *Metoda vytvářející Auto
     * 
     * @param nazev zadaný název auta
     * @param cena cena
     * @param ridicak potřebný řidičák k řízení
     * @param slozka adresář se soubory
     * @return true když bylo vytvořeno úspěšně, false v případě chyby
     */
    public static boolean vytvorAuto(String nazev, String cena, String ridicak, File slozka) {
        File soubor = new File(slozka.getAbsolutePath() + "\\" + nazev + ".auto");
        try {
            soubor.createNewFile();
        } catch (Exception e) {
            return false;
        }
        try (BufferedWriter zapis = new BufferedWriter(new FileWriter(soubor.getAbsolutePath(), true))) {
            zapis.append(nazev + "\n" + "Cena na den je: " + cena + "\n" + "Potřebný řidičský průkaz je: " + ridicak);
        } catch (Exception NelzeVytvorit) {
            throw new NelzeVytvorit(soubor);
        }
        return true;
    }

    /**
     * Ověření jestli je string číslo
     *
     * @param cislo číslo
     * @return return true když je číslo, jinak false
     */
    public static boolean jeCislo(String cislo) {
        try {
            Integer.parseInt(cislo);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Metada ověřující jestli zadaný řidičák existuje
     *
     * @param ridicak zadaný string
     * @return true když existuje, jinak false
     */
    public static boolean overRidicak(String ridicak) {
        if ("A".equals(ridicak) || "B".equals(ridicak) || "C".equals(ridicak) || "D".equals(ridicak)) {
            return true;
        }
        return false;
    }

    /**
     * Ověřuje jestli už auto s takovým názvem neexistuje
     *
     * @param nazev zadaný string
     * @param slozka adresář se soubory
     * @return true když neexistuje, jinak false
     */
    public static boolean overNazev(String nazev, File slozka) {
        File soubor = new File(slozka.getAbsolutePath() + "\\" + nazev + ".auto");
        if (soubor.exists()) {
            return false;
        }
        return true;
    }
}
