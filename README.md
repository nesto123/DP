# DP
[![CC BY-NC-SA 4.0][cc-shield]][cc]
[![stability-beta](https://img.shields.io/badge/stability-beta-33bbff.svg)](https://github.com/mkenney/software-guides/blob/master/STABILITY-BADGES.md#beta)


Global snapshot in document management system for university course [Distribuirani procesi](https://www.pmf.unizg.hr/math/predmet/dispro), academic year 2020/2021, University of Zagreb, Faculty of Science, Department of Mathematics.

Description :
> Globalna snimka stanja u
sustavu za upravljanje
dokumentima. Sustav za upravljanje dokumentima u nekom tijelu državne uprave sastoji se od više
poslužitelja. Na nalog svojeg korisnika, svaki poslužitelj u bilo kojem trenutku može: - stvoriti
kod sebe novi dokument, - brisati dokument koji se nalazi kod njega, - poslati dokument
drugom poslužitelju, - primiti dokument od drugog poslužitelja. Osim toga, jedan od
poslužitelja može u bilo kojem trenutku generirati globalnu snimku stanja - ona treba pokazati
koji sve dokumenti postoje u sustavu i gdje se oni nalaze. Pretpostavljamo da jedan dokument
postoji u samo jednom primjerku te se u svakom trenutku nalazi samo na jednom mjestu -
kod jednog poslužitelja ili u komunikacijskom kanalu između dva poslužitelja. Napravite
distribuiranu aplikaciju koja simulira rad ovog sustava. Svaki poslužitelj treba biti realiziran kao
jedan proces. Globalna snimka mora biti realizirana kao cjeloviti izvještaj na jednom mjestu,
dakle ne smije se sastojati od distribuiranih dijelova.

## Running instructions

This app is developed in [Eclipse IDE](https://www.eclipse.org/ide/). 
 **Prerequisite** for runing and/or editing: [Java JDK 16](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html), [JRE](https://www.java.com/en/download/manual.jsp).

Beta release of simulated distributed Document Management System app is avalible in [releases](https://github.com/nesto123/DP/releases).

To run app, follow this steps:
1. In terminal run *NameServer*:  `java -jar NameServer.jar`
2. In terminal run as many application instances as you want with:  `java -jar App.jar`
    * `App.jar` will ask you to enter process id and total number of processes.


## Licence
  
 [DP](https://github.com/nesto123/DP) © 2021 by [Fran Vojković](https://github.com/nesto123) and [Alen Živković](https://github.com/zialen) is licensed under [Attribution-NonCommercial-ShareAlike 4.0 International][cc].

[![CC 4.0][cc-image]][cc]


[cc]: https://creativecommons.org/licenses/by-nc-sa/4.0/?ref=chooser-v1
[cc-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png
[cc-shield]: https://img.shields.io/badge/License-CC%20BY--SA%204.0-lightgrey.svg


License can be found under [License](LICENSE).
