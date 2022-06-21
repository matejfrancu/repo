package osoby;

/**
 * Třídá pro vytvoření osoby
 *
 * @author Matěj Franců
 */
public class Osoba {

    private static final String[] TYPY_RIDICAKU = {"A", "B", "C", "D"};
    private String jmeno;
    private String prijmeni;
    private int cislo;
    private String ridicak;

    /**
     * Konstruktor osoby
     *
     * @param jmeno jméno
     * @param prijmeni příjmení
     * @param cislo telefonní číslo
     * @param ridicak dostupný řidičák
     */
    public Osoba(String jmeno, String prijmeni, int cislo, String ridicak) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.cislo = cislo;
        this.ridicak = ridicak;
    }

    /**
     * Metoda ověřující jestli řidičák existuje
     *
     * @param ridicak zadaný string
     * @return true když ano, false když ne
     */
    public static Boolean overRidicak(String ridicak) {
        for (int i = 0; i < TYPY_RIDICAKU.length; i++) {
            if (ridicak.equals(TYPY_RIDICAKU[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda ověřující jestli je zadaný string číslo a jestli je devíti místné
     *
     * @param x zadaný string
     * @return true když je, jinak false
     */
    public static Boolean overCislo(String x) {
        if (jeCislo(x)) {
            int cislo = Integer.parseInt(x);
            if ((cislo / 100000000) < 1 || (cislo / 100000000) > 9) {
                return false;
            }
            return true;
        }
        return false;
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

    public String getPrijmeni() {
        return prijmeni;
    }

    public String getJmeno() {
        return jmeno;
    }

    public int getCislo() {
        return cislo;
    }

    public String getRidicak() {
        return ridicak;
    }

    @Override
    public String toString() {
        return jmeno + ";" + prijmeni + ";" + cislo + ";" + ridicak;
    }

}
