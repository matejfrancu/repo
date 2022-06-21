package auta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import vyjimky.NelzeSmazat;

/**
 * Třída pracující s objektem Auto
 *
 * @author Matěj Franců
 */
public class Auto {

    private File soubor;

    /**
     * Konstruktor, vytváří objekt Auto
     *
     * @param soubor
     */
    public Auto(File soubor) {
        this.soubor = soubor;
    }

    /**
     * Metoda pro výpis názvů všech aut
     *
     * @param slozka adresář se soubory
     * @return pole stringů s názvy aut
     */
    public static String[] vypisAutNazev(File slozka) {
        File[] seznamSouboru = slozka.listFiles();
        String[] auta = new String[seznamSouboru.length];
        int j = 0;
        for (int i = 0; i < seznamSouboru.length; i++) {
            if (spravnaPripona(seznamSouboru[i])) {
                auta[i] = new Auto(seznamSouboru[i]).getNazev();
                j++;
            }
        }
        return auta;
    }

    /**
     * Getter pro soubor
     *
     * @return soubor Auta
     */
    public File getSoubor() {
        return soubor;
    }

    /**
     * Metoda pro výpis aut
     *
     * @param slozka adresář se soubory
     * @return pole všech Aut
     */
    public static Auto[] vypisAut(File slozka) {
        File[] seznamSouboru = slozka.listFiles();
        List<Auto> list = new ArrayList<Auto>();
        for (int i = 0; i < seznamSouboru.length; i++) {
            if (spravnaPripona(seznamSouboru[i])) {
                String[] x = seznamSouboru[i].getName().split("_");
                list.add(new Auto(seznamSouboru[i]));
            }
        }
        Auto[] vysledek = new Auto[list.size()];
        vysledek = list.toArray(vysledek);
        return vysledek;
    }

    /**
     * Pomocná metoda pro určení přípony auto u souboru
     *
     * @param slozka adresář se soubory
     * @return true když má takovou příponu, jinak false
     */
    private static boolean spravnaPripona(File soubor) {
        String extension = "";
        int x = soubor.getAbsolutePath().lastIndexOf('.');
        if (x > 0) {
            extension = soubor.getAbsolutePath().substring(x + 1);
        }
        if (extension.equals("auto")) {
            return true;
        }
        return false;
    }

    /**
     * Metoda pro getnutí názvu auta
     *
     * @return String s názvem, nebo null při chybě čtení ze souboru
     */
    public String getNazev() {
        try (BufferedReader nacitac = new BufferedReader(new FileReader(this.soubor))) {
            return nacitac.readLine();
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public String toString() {
        try (BufferedReader nacitac = new BufferedReader(new FileReader(this.soubor))) {
            return nacitac.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /**
     * Metoda pro načtení nového Auta
     *
     * @param nazev zadaný název auta
     * @param slozka adresář se soubory
     * @return nově načtené Auto
     */
    public static Auto nacteniAuta(String nazev, File slozka) {
        File[] seznamSouboru = slozka.listFiles();
        for (int i = 0; i < seznamSouboru.length; i++) {
            Auto auto = new Auto(seznamSouboru[i]);
            if ((auto.getNazev()).equals(nazev)) {
                return new Auto(seznamSouboru[i]);
            }
        }
        return null;
    }

    /**
     * Metoda vracející potřebný řidičák pro řízení Auta
     *
     * @param auto auto
     * @return string s řidičákem
     */
    public static String getRidicak(Auto auto) {
        String tostring = auto.toString();
        String[] pom = tostring.split("[\n]");
        String[] pom2 = pom[2].split("[ ]");
        return pom2[pom2.length - 1];
    }

    /**
     * Metoda vracející cenu auta na den
     *
     * @param auto auto
     * @return int s cenou
     */
    public static int getCena(Auto auto) {
        String tostring = auto.toString();
        String[] pom = tostring.split("[\n]");
        String[] pom2 = pom[1].split("[ ]");
        return Integer.parseInt(pom2[pom2.length - 1].trim());
    }

    /**
     * Metoda pro smazání souboru Auta
     *
     * @return true když jde úspěšně smazat, jinak vrací chybu
     */
    public boolean smazatAuto() {
        if (!this.soubor.delete()) {
            throw new NelzeSmazat(this.soubor);
        }
        return true;
    }

    /**
     * Metoda pro výpis aut dle ceny
     *
     * @param slozka adresář se soubory
     * @return pole aut
     */
    public static Auto[] vypisAutCena(File slozka) {
        File[] seznamSouboru = slozka.listFiles();
        List<Auto> list = new ArrayList<Auto>();
        for (int i = 0; i < seznamSouboru.length; i++) {
            if (spravnaPripona(seznamSouboru[i])) {
                String[] x = seznamSouboru[i].getName().split("_");
                list.add(new Auto(seznamSouboru[i]));
            }
        }
        Auto[] vysledek = new Auto[list.size()];
        Collections.sort(list, KOMPARATOR_DLE_CENY);
        vysledek = list.toArray(vysledek);
        return vysledek;
    }

    public static final Comparator<Auto> KOMPARATOR_DLE_CENY = new Comparator<Auto>() {
        @Override
        public int compare(Auto auto1, Auto auto2) {
            int a1 = getCena(auto1);
            int a2 = getCena(auto2);
            if (a1 > a2) {
                return 1;
            } else if (a1 < a2) {
                return -1;
            }
            return 0;
        }
    };
}
