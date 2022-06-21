package apps;

import auta.Auto;
import osoby.Osoba;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import rezervace.Rezervace;

/**
 *
 * @author Matěj Franců
 */
public class PujcovnaApp {

    private static final Scanner sc = new Scanner(System.in);
    private static File adresar;
    private static final Locale LOC_CZ = new Locale("cs", "CZ");
    private static final Locale LOC_EN = Locale.US;
    private static final ResourceBundle RB_CZ = ResourceBundle.getBundle("popisky\\popisky_CZ", LOC_CZ);
    private static final ResourceBundle RB_EN = ResourceBundle.getBundle("popisky\\popisky_EN", LOC_EN);
    private static ResourceBundle usedRB = RB_CZ;

    public static void main(String[] args) {
        vyberJazyk();
        nactiCestuDoAdresare();
        //HLAVNÍ MENU
        boolean konecProgramu = false;
        while (!konecProgramu) {
            vypisHlavniMenuProgramu();
            int volbaUzivatele = nacistVolbu();
            switch (volbaUzivatele) {
                case 0:
                    konecProgramu = true;
                    break;
                case 1:
                    rezervaceMenu();
                    break;
                case 2:
                    spravaAutMenu();
                    break;
                default:
                    System.out.println(usedRB.getString("neplatnaVolba"));
            }
        }
    }

    private static void vypisHlavniMenuProgramu() {
        System.out.println(usedRB.getString("volba0"));
        System.out.println(usedRB.getString("volba1"));
        System.out.println(usedRB.getString("volba2"));
    }

    private static void rezervaceMenu() {
        boolean konec = false;
        while (!konec) {
            vypisMenuRezervace();
            int volbaUzivatele = nacistVolbu();
            switch (volbaUzivatele) {
                case 0:
                    konec = true;
                    break;
                case 1:
                    vypisAut();
                    break;
                case 2:
                    vypisAutCena();
                    break;
                case 3:
                    vypisAutDatum();
                    break;
                case 4:
                    vytvorRezervaci();
                    break;
                default:
                    System.out.println(usedRB.getString("neplatnaVolba"));
            }
        }
    }

    private static void vypisMenuRezervace() {
        System.out.println(usedRB.getString("volbaRezervace0"));
        System.out.println(usedRB.getString("volbaRezervace1"));
        System.out.println(usedRB.getString("volbaRezervace2"));
        System.out.println(usedRB.getString("volbaRezervace3"));
        System.out.println(usedRB.getString("volbaRezervace4"));
    }

    private static void spravaAutMenu() {
        System.out.println("");
        boolean konec = false;
        while (!konec) {
            vypisSpravaAutMenu();
            int volbaUzivatele = nacistVolbu();
            switch (volbaUzivatele) {
                case 0:
                    konec = true;
                    break;
                case 1:
                    vypisAut();
                    break;
                case 2:
                    vytvorAuto();
                    break;
                case 3:
                    smazatAuto();
                    break;
                case 4:
                    vypisRezervaci();
                    break;
                case 5:
                    zrusitRezervaci();
                    break;
                default:
                    System.out.println(usedRB.getString("neplatnaVolba"));
            }
        }
    }

    private static void vypisSpravaAutMenu() {
        System.out.println(usedRB.getString("menuSpravy0"));
        System.out.println(usedRB.getString("menuSpravy1"));
        System.out.println(usedRB.getString("menuSpravy2"));
        System.out.println(usedRB.getString("menuSpravy3"));
        System.out.println(usedRB.getString("menuSpravy4"));
        System.out.println(usedRB.getString("menuSpravy5"));
    }

    private static void nactiCestuDoAdresare() {
        System.out.println(usedRB.getString("nactiCestu"));
        String cesta = sc.nextLine();
        adresar = new File(cesta);
        boolean konec = false;
        while (!konec) {
            if (adresar.exists() && adresar.isDirectory()) {
                System.out.println(usedRB.getString("uspesnaCesta"));
                konec = true;
            } else {
                System.out.println(usedRB.getString("neuspesnaCesta"));
                System.out.println(usedRB.getString("nactiPlatnouCestu"));
                cesta = sc.nextLine();
                adresar = new File(cesta);
            }
        }
    }

    private static int nacistVolbu() {
        int volba = -1;
        try {
            volba = sc.nextInt();
        } catch (InputMismatchException e) {
            volba = -1;
        } finally {
            sc.nextLine();
        }
        return volba;
    }

    private static void vytvorAuto() {
        System.out.println(usedRB.getString("nazevAuta"));
        String nazev = sc.nextLine();
        if (!auta.AutoSoubor.overNazev(nazev, adresar)) {
            do {
                System.out.println(usedRB.getString("nazevNeplatny"));
                nazev = sc.nextLine();
            } while (!auta.AutoSoubor.overNazev(nazev, adresar));
        }
        System.out.println(usedRB.getString("zadejCenu"));
        String cena = sc.nextLine();
        if (!auta.AutoSoubor.jeCislo(cena)) {
            do {
                System.out.println(usedRB.getString("zadejCenu"));
                cena = sc.nextLine();
            } while (!auta.AutoSoubor.jeCislo(cena));
        }
        System.out.println(usedRB.getString("zadejRidicak"));
        String ridicak = sc.nextLine();
        if (!auta.AutoSoubor.overRidicak(ridicak)) {
            do {
                System.out.println(usedRB.getString("neplatnyRidicak"));
                ridicak = sc.nextLine();
            } while (!auta.AutoSoubor.overRidicak(ridicak));
        }
        if (auta.AutoSoubor.vytvorAuto(nazev, cena, ridicak, adresar)) {
            System.out.println(usedRB.getString("autoVytvoreno"));
        } else {
            System.out.println(usedRB.getString("autoNevytvoreno"));
        }
    }

    private static void vytvorRezervaci() {
        System.out.println(usedRB.getString("jmeno"));
        String jmeno = sc.nextLine();
        System.out.println(usedRB.getString("prijmeni"));
        String prijmeni = sc.nextLine();
        System.out.println(usedRB.getString("cislo"));
        String cislo = sc.nextLine();
        if (!osoby.Osoba.overCislo(cislo)) {
            do {
                System.out.println(usedRB.getString("neplatneCislo"));
                cislo = sc.nextLine();
            } while (!osoby.Osoba.overCislo(cislo));
        }
        int cisloint = Integer.parseInt(cislo);
        System.out.println(usedRB.getString("ridicak"));
        String ridicak = sc.nextLine();
        if (!osoby.Osoba.overRidicak(ridicak)) {
            do {
                System.out.println(usedRB.getString("neplatnyRidicak"));
                ridicak = sc.nextLine();
            } while (!osoby.Osoba.overRidicak(ridicak));
        }
        Osoba osoba = new Osoba(jmeno, prijmeni, cisloint, ridicak);
        System.out.println(usedRB.getString("nazevAuta"));
        String nazev = sc.nextLine();
        Auto auto = null;
        if (Auto.nacteniAuta(nazev, adresar) == null) {
            do {
                System.out.println(usedRB.getString("autoNeexistuje"));
                nazev = sc.nextLine();
            } while (Auto.nacteniAuta(nazev, adresar) == null);
            auto = Auto.nacteniAuta(nazev, adresar);
        } else {
            auto = Auto.nacteniAuta(nazev, adresar);
        }
        boolean konec = false;
        boolean pokracovat = true;
        if (!ridicak.equals(auta.Auto.getRidicak(auto))) {
            do {
                System.out.println(usedRB.getString("neplatnyRidicakAuto"));
                System.out.println(usedRB.getString("zrusit"));
                System.out.println(usedRB.getString("noveAuto2"));
                int volbaUzivatele = nacistVolbu();
                switch (volbaUzivatele) {
                    case 1:
                        System.out.println(usedRB.getString("nazevAuta"));
                        nazev = sc.nextLine();
                        auto = null;
                        if (Auto.nacteniAuta(nazev, adresar) == null) {
                            do {
                                System.out.println(usedRB.getString("autoNeexistuje"));
                                nazev = sc.nextLine();
                            } while (Auto.nacteniAuta(nazev, adresar) == null);
                            auto = Auto.nacteniAuta(nazev, adresar);
                        } else {
                            auto = Auto.nacteniAuta(nazev, adresar);
                        }
                        if (ridicak.equals(auta.Auto.getRidicak(auto))) {
                            konec = true;
                        }
                        break;
                    case 0:
                        konec = true;
                        pokracovat = false;
                        break;
                    default:
                        System.out.println(usedRB.getString("neplatnaVolba"));
                }
            } while (!ridicak.equals(auta.Auto.getRidicak(auto)) && konec == false);
        }
        if (pokracovat == true) {
            System.out.println(usedRB.getString("odkdy"));
            String odkdypom = sc.nextLine();
            int[] odkdy = Rezervace.overDatum(odkdypom);
            if (Rezervace.overDatum(odkdypom) == null) {
                do {
                    System.out.println(usedRB.getString("neplatneDatum"));
                    odkdypom = sc.nextLine();
                } while (Rezervace.overDatum(odkdypom) == null);
                odkdy = Rezervace.overDatum(odkdypom);
            }
            System.out.println(usedRB.getString("dokdy"));
            String dokdypom = sc.nextLine();
            int[] dokdy = Rezervace.overDatum(dokdypom);
            if (Rezervace.overDatum(dokdypom) == null) {
                do {
                    System.out.println(usedRB.getString("neplatneDatum"));
                    dokdypom = sc.nextLine();
                } while (Rezervace.overDatum(dokdypom) == null);
                dokdy = Rezervace.overDatum(dokdypom);
            }
            if (!Rezervace.datumPo(odkdy, dokdy)) {
                do {
                    System.out.println(usedRB.getString("neplatneDatum"));
                    dokdypom = sc.nextLine();
                } while (Rezervace.overDatum(dokdypom) == null && !Rezervace.datumPo(odkdy, dokdy));
                dokdy = Rezervace.overDatum(dokdypom);
            }
            konec = false;
            if (!Rezervace.dostupnostAuta(adresar, odkdy, dokdy, auto)) {
                do {
                    vypisDostupnostMenu();
                    int volbaUzivatele = nacistVolbu();
                    switch (volbaUzivatele) {
                        case 1:
                            System.out.println(usedRB.getString("odkdy"));
                            odkdypom = sc.nextLine();
                            odkdy = Rezervace.overDatum(odkdypom);
                            if (Rezervace.overDatum(odkdypom) == null) {
                                do {
                                    System.out.println(usedRB.getString("neplatneDatum"));
                                    odkdypom = sc.nextLine();
                                } while (Rezervace.overDatum(odkdypom) == null);
                                odkdy = Rezervace.overDatum(odkdypom);
                            }
                            System.out.println(usedRB.getString("dokdy"));
                            dokdypom = sc.nextLine();
                            dokdy = Rezervace.overDatum(dokdypom);
                            if (Rezervace.overDatum(dokdypom) == null) {
                                do {
                                    System.out.println(usedRB.getString("neplatneDatum"));
                                    dokdypom = sc.nextLine();
                                } while (Rezervace.overDatum(dokdypom) == null);
                                dokdy = Rezervace.overDatum(dokdypom);
                            }
                            if (!Rezervace.datumPo(odkdy, dokdy)) {
                                do {
                                    System.out.println(usedRB.getString("neplatneDatum"));
                                    dokdypom = sc.nextLine();
                                } while (Rezervace.overDatum(dokdypom) == null && !Rezervace.datumPo(odkdy, dokdy));
                                dokdy = Rezervace.overDatum(dokdypom);
                            }
                            if (Rezervace.dostupnostAuta(adresar, odkdy, dokdy, auto)) {
                                Rezervace.vytvorRezervaci(osoba, odkdy, dokdy, auto, adresar);
                                System.out.println(usedRB.getString("rezervaceUspech"));
                                System.out.println(usedRB.getString("vyslednaCena") + Rezervace.vytvorCenu(auto, odkdy, dokdy) + " Kč");
                                konec = true;
                            }
                            break;
                        case 2:
                            System.out.println(usedRB.getString("nazevAuta"));
                            nazev = sc.nextLine();
                            auto = null;
                            if (Auto.nacteniAuta(nazev, adresar) == null) {
                                do {
                                    System.out.println(usedRB.getString("nazevNeplatny"));
                                    nazev = sc.nextLine();
                                } while (Auto.nacteniAuta(nazev, adresar) == null);
                                auto = Auto.nacteniAuta(nazev, adresar);
                            } else {
                                auto = Auto.nacteniAuta(nazev, adresar);
                            }
                            if (Rezervace.dostupnostAuta(adresar, odkdy, dokdy, auto)) {
                                Rezervace.vytvorRezervaci(osoba, odkdy, dokdy, auto, adresar);
                                System.out.println(usedRB.getString("rezervaceUspech"));
                                System.out.println(usedRB.getString("vyslednaCena") + Rezervace.vytvorCenu(auto, odkdy, dokdy) + " Kč");
                                konec = true;
                            }
                            break;
                        case 0:
                            konec = true;
                            break;
                        default:
                            System.out.println(usedRB.getString("neplatnaVolba"));
                    }
                } while (!Rezervace.dostupnostAuta(adresar, odkdy, dokdy, auto) && !konec);
            } else {
                Rezervace.vytvorRezervaci(osoba, odkdy, dokdy, auto, adresar);
                System.out.println(usedRB.getString("rezervaceUspech"));
                System.out.println(usedRB.getString("vyslednaCena") + Rezervace.vytvorCenu(auto, odkdy, dokdy) + " Kč");
            }
        }
    }

    private static void vypisAut() {
        Auto[] auta = Auto.vypisAut(adresar);
        for (int i = 0; i < auta.length; i++) {
            System.out.println(auta[i].toString());
            System.out.println("");
        }
    }

    private static void smazatAuto() {
        System.out.println(usedRB.getString("nazevAutaSmazani"));
        String nazev = sc.nextLine();
        Auto auto = null;
        if (Auto.nacteniAuta(nazev, adresar) == null) {
            do {
                System.out.println(usedRB.getString("autoNeexistuje"));
                nazev = sc.nextLine();
            } while (Auto.nacteniAuta(nazev, adresar) == null);
            auto = Auto.nacteniAuta(nazev, adresar);
        } else {
            auto = Auto.nacteniAuta(nazev, adresar);
        }
        if (auto.smazatAuto()) {
            System.out.println(usedRB.getString("autoSmazano"));
        } else {
            System.out.println(usedRB.getString("autoNesmazano"));
        }

    }

    private static void vypisAutDatum() {
        System.out.println(usedRB.getString("odkdy"));
        String odkdypom = sc.nextLine();
        int[] odkdy = Rezervace.overDatum(odkdypom);
        if (Rezervace.overDatum(odkdypom) == null) {
            do {
                System.out.println(usedRB.getString("neplatneDatum"));
                odkdypom = sc.nextLine();
            } while (Rezervace.overDatum(odkdypom) == null);
            odkdy = Rezervace.overDatum(odkdypom);
        }
        System.out.println(usedRB.getString("dokdy"));
        String dokdypom = sc.nextLine();
        int[] dokdy = Rezervace.overDatum(dokdypom);
        if (Rezervace.overDatum(dokdypom) == null) {
            do {
                System.out.println(usedRB.getString("neplatneDatum"));
                dokdypom = sc.nextLine();
            } while (Rezervace.overDatum(dokdypom) == null);
            dokdy = Rezervace.overDatum(dokdypom);
        }
        if (!Rezervace.datumPo(odkdy, dokdy)) {
            do {
                System.out.println(usedRB.getString("neplatneDatum"));
                dokdypom = sc.nextLine();
            } while (Rezervace.overDatum(dokdypom) == null && !Rezervace.datumPo(odkdy, dokdy));
            dokdy = Rezervace.overDatum(dokdypom);
        }
        String[] auta = Rezervace.autoDatum(adresar, odkdy, dokdy);
        System.out.println(usedRB.getString("dostupnaAuta"));
        for (int i = 0; i < auta.length; i++) {
            if (!"ne".equals(auta[i]) && auta[i] != null) {
                System.out.println(auta[i]);
            }
        }

    }

    private static void vypisDostupnostMenu() {
        System.out.println(usedRB.getString("nedostupne"));
        System.out.println(usedRB.getString("zrusit"));
        System.out.println(usedRB.getString("noveDatum"));
        System.out.println(usedRB.getString("noveAuto"));
    }

    private static void vyberJazyk() {
        System.out.println(usedRB.getString("jazykNadpis"));
        System.out.println(usedRB.getString("cesky"));
        System.out.println(usedRB.getString("anglicky"));
        int volba;
        do {
            volba = nacistVolbu();
            if (volba == 1) {
                System.out.println(usedRB.getString("cestinaNastavena"));
                usedRB = RB_CZ;
                break;
            } else if (volba == 2) {
                System.out.println(usedRB.getString("anglictinaNastavena"));
                usedRB = RB_EN;
                break;
            }
            System.out.println(usedRB.getString("neplatnaVolba"));
        } while (volba != 1 || volba != 2);
    }

    private static void vypisRezervaci() {
        String[] pom = Rezervace.vypsatRezervace(adresar);
        for (int i = 0; i < pom.length; i++) {
            System.out.println(pom[i]);
            System.out.println("");
        }
    }

    private static void zrusitRezervaci() {
        System.out.println(usedRB.getString("prijmeniZrusit"));
        String prijmeni = sc.nextLine();
        System.out.println(usedRB.getString("odkdyZrusit"));
        String odkdypom = sc.nextLine();
        int[] odkdy = Rezervace.overDatum(odkdypom);
        if (Rezervace.overDatum(odkdypom) == null) {
            do {
                System.out.println(usedRB.getString("neplatneDatum"));
                odkdypom = sc.nextLine();
            } while (Rezervace.overDatum(odkdypom) == null);
            odkdy = Rezervace.overDatum(odkdypom);
        }
        if (Rezervace.smazatRezervaciKontrola(adresar, prijmeni, odkdy) != null) {
            if (Rezervace.smazatRezervaci(Rezervace.smazatRezervaciKontrola(adresar, prijmeni, odkdy))) {
                System.out.println(usedRB.getString("rezervaceSmazano"));
            } else {
                System.out.println(usedRB.getString("rezervaceNesmazano"));
            }
        } else {
            System.out.println(usedRB.getString("neni"));
        }
    }

    private static void vypisAutCena() {
        Auto[] auta = Auto.vypisAutCena(adresar);
        for (int i = 0; i < auta.length; i++) {
            System.out.println(auta[i].toString());
            System.out.println("");
        }
    }

}
