package rezervace;

import auta.Auto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.stream.Collectors;
import osoby.Osoba;
import vyjimky.NelzeSmazat;
import vyjimky.NelzeVytvorit;

/**
 *
 * @author Matěj Franců
 */
public class Rezervace {

    private static final int[] POCTY_DNU_V_MESICI = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * Metoda vytvářející soubor rezervace
     *
     * @param osoba objekt osoba
     * @param odkdy datum od kdy
     * @param dokdy datum do kdy
     * @param auto objekt auto
     * @param slozka adresář se soubory
     * @return true když je úspěšně vytvořen, jinak false
     */
    public static boolean vytvorRezervaci(Osoba osoba, int[] odkdy, int[] dokdy, Auto auto, File slozka) {
        String cesta = vygenerujCestu(odkdy, osoba.getPrijmeni(), slozka);
        String autonazev = auto.getNazev();
        File soubor = new File(cesta);
        try {
            soubor.createNewFile();
        } catch (Exception e) {
            return false;
        }
        try (BufferedWriter zapis = new BufferedWriter(new FileWriter(soubor.getAbsolutePath(), true))) {
            zapis.append(odkdy[0] + "/" + odkdy[1] + "/" + odkdy[2] + "-" + dokdy[0] + "/" + dokdy[1] + "/" + dokdy[2] + "|" + autonazev + "|" + osoba.toString());
        } catch (Exception NelzeVytvorit) {
            throw new NelzeVytvorit(soubor);
        }
        return true;
    }

    /**
     * Metoda ověřující jestli je string platné datum
     *
     * @param datum zadaný string
     * @return pole integerů DD/MM/RRRR
     */
    public static int[] overDatum(String datum) {
        String[] datumSplit = datum.split("[-./_]");
        if (datumSplit.length == 3) {
            if (jeCislo(datumSplit[0]) && jeCislo(datumSplit[1]) && jeCislo(datumSplit[2])) {
                if (jePlatneDatum(Integer.parseInt(datumSplit[0]), Integer.parseInt(datumSplit[1]), Integer.parseInt(datumSplit[2]))) {
                    return new int[]{Integer.parseInt(datumSplit[0]), Integer.parseInt(datumSplit[1]), Integer.parseInt(datumSplit[2])};
                }
            }
        }
        return null;
    }

    /**
     * Metoda generující cestu ve formátu RRRR_MM_DD_prijmeni
     *
     * @param datum pole integerů obsahující datum DD/MM/RRRR
     * @return pole integerů DD/MM/RRRR
     */
    private static String vygenerujCestu(int[] datum, String nazev, File slozka) {
        String cesta = slozka.getAbsolutePath() + "\\";
        cesta += datum[2] + "_";
        if (datum[1] < 10) {
            cesta += "0" + datum[1] + "_";
        } else {
            cesta += datum[1] + "_";
        }
        if (datum[0] < 10) {
            cesta += "0" + datum[0] + "_";
        } else {
            cesta += datum[0] + "_";
        }
        String nazevFormatovany = nazev.replaceAll(" ", "_").toLowerCase();
        cesta += nazevFormatovany + ".rezervace";
        return cesta;
    }

    /**
     * Pomocná metoda ověřující zda je String číslo
     *
     * @param cislo zadaný string
     * @return vrací true v případě že se jedá o číslo
     */
    private static boolean jeCislo(String cislo) {
        try {
            Integer.parseInt(cislo);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Metoda ověřující platnost datumu
     *
     * @param den den
     * @param mesic měsíc
     * @param rok rok
     * @return true v případě že je platné
     */
    private static boolean jePlatneDatum(int den, int mesic, int rok) {
        if (mesic < 1) {
            return false;
        }
        if (mesic > 12) {
            return false;
        }
        if (den < 1) {
            return false;
        }
        if (den > pocetDnuVMesici(mesic, rok)) {
            return false;
        }
        return true;
    }

    /**
     * Ověřuje správnost dní v měsící
     *
     * @param mesic mesic
     * @param rok rok
     * @return
     */
    private static int pocetDnuVMesici(int mesic, int rok) {
        int pdm = POCTY_DNU_V_MESICI[mesic - 1];
        return pdm;
    }

    /**
     * Metoda pro kontrolu jestli je auto v zadaném rozpětí dnů volné
     *
     * @param slozka adresář se soubory
     * @param odkdypole pole integeru s datumem odkdy
     * @param dokdypole pole integeru s datumem dokdy
     * @param auto objekt auto
     * @return true když je volné, false když není
     */
    public static boolean dostupnostAuta(File slozka, int[] odkdypole, int[] dokdypole, Auto auto) {
        File[] seznamSouboru = slozka.listFiles();
        String pom = null;
        String[] pomstring = null;
        int odkdy = 0, dokdy = 0, odkdy2 = 0, dokdy2 = 0;
        for (int i = 0; i < seznamSouboru.length; i++) {
            if (spravnaPripona(seznamSouboru[i])) {
                try (BufferedReader nacitac = new BufferedReader(new FileReader(seznamSouboru[i]))) {
                    pom = nacitac.readLine();
                } catch (Exception e) {
                }
                pomstring = pom.split("[|]");
                String[] pomstring2 = pomstring[0].split("[-]");
                int[] odkdypole2 = overDatum(pomstring2[0]);
                int[] dokdypole2 = overDatum(pomstring2[1]);
                String autorezervace = pomstring[1];
                odkdy = odkdypole[0] + odkdypole[1] * POCTY_DNU_V_MESICI[odkdypole[1] - 1] + odkdypole[2];
                dokdy = dokdypole[0] + dokdypole[1] * POCTY_DNU_V_MESICI[dokdypole[1] - 1] + dokdypole[2];
                odkdy2 = odkdypole2[0] + odkdypole2[1] * POCTY_DNU_V_MESICI[odkdypole2[1] - 1] + odkdypole2[2];
                dokdy2 = dokdypole2[0] + dokdypole2[1] * POCTY_DNU_V_MESICI[dokdypole2[1] - 1] + dokdypole2[2];
                if ((odkdy <= dokdy2) && (odkdy2 <= dokdy)) {
                    if (autorezervace.equals(auto.getNazev())) {
                        return false;
                    }
                }
            }
        }
        return true;
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
        if (extension.equals("rezervace")) {
            return true;
        }
        return false;
    }

    /**
     * Metoda vracející všechna auta která jsou v zadaném rozpětí volná
     *
     * @param slozka adresář se soubory 
     * @param odkdypole pole integeru s datumem odkdy
     * @param dokdypole pole integeru s datumem dokdy
     * @return pole stringů s nazvy volných aut
     */
    public static String[] autoDatum(File slozka, int[] odkdypole, int[] dokdypole) {
        File[] seznamSouboru = slozka.listFiles();
        String pom = null;
        String[] pomstring = null;
        int odkdy = 0, dokdy = 0, odkdy2 = 0, dokdy2 = 0;
        String[] auta = Auto.vypisAutNazev(slozka);
        for (int i = 0; i < seznamSouboru.length; i++) {
            if (spravnaPripona(seznamSouboru[i])) {
                try (BufferedReader nacitac = new BufferedReader(new FileReader(seznamSouboru[i]))) {
                    pom = nacitac.readLine();
                } catch (Exception e) {
                }
                pomstring = pom.split("[|]");
                String[] pomstring2 = pomstring[0].split("[-]");
                int[] odkdypole2 = overDatum(pomstring2[0]);
                int[] dokdypole2 = overDatum(pomstring2[1]);
                String autorezervace = pomstring[1];
                odkdy = odkdypole[0] + odkdypole[1] * POCTY_DNU_V_MESICI[odkdypole[1] - 1] + odkdypole[2];
                dokdy = dokdypole[0] + dokdypole[1] * POCTY_DNU_V_MESICI[dokdypole[1] - 1] + dokdypole[2];
                odkdy2 = odkdypole2[0] + odkdypole2[1] * POCTY_DNU_V_MESICI[odkdypole2[1] - 1] + odkdypole2[2];
                dokdy2 = dokdypole2[0] + dokdypole2[1] * POCTY_DNU_V_MESICI[dokdypole2[1] - 1] + dokdypole2[2];
                if ((odkdy <= dokdy2) && (odkdy2 <= dokdy)) {
                    for (int z = 0; z < auta.length; z++) {
                        if (autorezervace.equals(auta[z])) {
                            auta[z] = "ne";
                        }
                    }
                }
            }
        }
        return auta;
    }

    /**
     * Metoda pro získání stringu z toho co je zapsané v souboru
     *
     * @param soubor adresář se soubory
     * @return string obsahující vše v souboru, null když se čtení nepovede
     */
    public String toString(File soubor) {
        try (BufferedReader nacitac = new BufferedReader(new FileReader(soubor))) {
            return nacitac.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /**
     * Metoda vytvářející výslednou cenu vypůjčení vozidla
     *
     * @param auto objekt auto
     * @param odkdypole pole integeru s datumem odkdy
     * @param dokdypole pole integeru s datumem dokdy
     * @return int s výslednou cenou
     */
    public static int vytvorCenu(Auto auto, int[] odkdypole, int[] dokdypole) {
        return ((dokdypole[0] + dokdypole[1] * POCTY_DNU_V_MESICI[dokdypole[1] - 1]) - (odkdypole[0] + odkdypole[1] * POCTY_DNU_V_MESICI[odkdypole[1] - 1])) * auta.Auto.getCena(auto);
    }

    /**
     * Metoda kontrolující jestli je druhé datum větší než první
     *
     * @param odkdypole pole integeru s datumem odkdy
     * @param dokdypole pole integeru s datumem dokdy
     * @return true když je, jinak false
     */
    public static boolean datumPo(int[] odkdypole, int[] dokdypole) {
        if ((dokdypole[0] + dokdypole[1] * POCTY_DNU_V_MESICI[dokdypole[1] - 1] + dokdypole[2] * 365) - (odkdypole[0] + odkdypole[1] * POCTY_DNU_V_MESICI[odkdypole[1] - 1] + odkdypole[2] * 365) <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Metoda pro vypsání obsahu souborů rezervací
     *
     * @param slozka adresář se soubory
     * @return pole stringů s obsahem souborů rezervací
     */
    public static String[] vypsatRezervace(File slozka) {
        File[] seznamSouboru = slozka.listFiles();
        int j = 0;
        for (int i = 0; i < seznamSouboru.length; i++) {
            if (spravnaPripona(seznamSouboru[i])) {
                j++;
            }
        }
        String[] pom = new String[j];
        for (int i = 0; i < j; i++) {
            if (spravnaPripona(seznamSouboru[i])) {
                try (BufferedReader nacitac = new BufferedReader(new FileReader(seznamSouboru[i]))) {
                    pom[i] = nacitac.readLine();
                } catch (Exception e) {
                }
            }
        }
        return pom;
    }

    /**
     * Metoda kontrolující jestli zadaná rezervace existuje
     *
     * @param slozka adresář se soubory
     * @param prijmeni string s příjmením 
     * @param odkdy pole integeru s datumem odkdy
     * @return File souboru rezervace, null když neexistuje
     */
    public static File smazatRezervaciKontrola(File slozka, String prijmeni, int[] odkdy) {
        File[] seznamSouboru = slozka.listFiles();
        for (int i = 0; i < seznamSouboru.length; i++) {
            if (spravnaPripona(seznamSouboru[i])) {
                String[] pom = seznamSouboru[i].getName().split("[.]");
                String[] pom2 = pom[0].split("[_]");
                int[] datum = new int[3];
                datum[0] = Integer.parseInt(pom2[2]);
                datum[1] = Integer.parseInt(pom2[1]);
                datum[2] = Integer.parseInt(pom2[0]);
                if (datum[0] == odkdy[0] && datum[1] == odkdy[1] && datum[2] == odkdy[2] && pom2[3].equals(prijmeni)) {
                    return seznamSouboru[i];
                }
            }
        }
        return null;
    }

    /**
     * Metoda pro smazání souboru rezervace
     *
     * @param soubor adresář se soubory
     * @return true když je smazaná úspěšně, jinak vyhodí chybu
     */
    public static boolean smazatRezervaci(File soubor) {
        if (!soubor.delete()) {
            throw new NelzeSmazat(soubor);
        }
        return true;
    }
}
