# Allegro_Summer_E-Xperiance_2021_task
### Zadanie nr 3. Software Engineer 
##### Cel zadania:
Stwórz oprogramowanie pozwalające na:
- listowanie repozytoriów (nazwa i liczba gwiazdek).
- zwracanie sumy gwiazdek we wszystkich repozytoriach.  

dla dowolnego użytkownika serwisu GitHub.  
Dane powinny być zwracane za pomocą protokołu HTTP.

##### Wymagania niefunkcjonalne:
- Wybierz jeden z języków programowania: Java/Kotlin/Python.
- Możesz korzystać z dowolnych bibliotek i frameworków do tworzenia aplikacji.
- Rozwiązanie powinno być zaimplementowane jako aplikacja serwerowa.
- Rozwiązanie posiada instrukcję instalacji/uruchomienia zamieszczoną w README.md.
- Wszystkie propozycje na późniejsze rozszerzenie/uwagi do rozwiązania projektu umieść w README.md.

### Instrukcja Uruchomienia
1.<b>Maven</b>: Wymagany jest zainstalowany <a href="https://maven.apache.org/index.html">maven</a>. W konsoli wpisujemy:

```
git clone https://github.com/mdabrow9/Allegro_Summer_E-Xperiance_2021_task.git
cd Allegro_Summer_E-Xperiance_2021_task
mvn spring-boot:runt
```
#### Testy
```
mvn test
```
2. <b>IntelliJ IDEA</b>:
  - File -> New ->Project from version Control...
  - podajemy url:  https://github.com/mdabrow9/Allegro_Summer_E-Xperiance_2021_task.git -> Clone
  - Uruchamiamy przyciskiem "RUN" lub domyślnym skrótem SHIFT + F10

#### Testy
- CTRL + SHIFT + F10
- Project -> Allegro_Summer_E-Xperiance_2021_task -> PPM -> Run 'All Tests'

3. <b>konsola</b>: Uruchamiamy poleceniami:
```
git clone https://github.com/mdabrow9/Allegro_Summer_E-Xperiance_2021_task.git
cd Allegro_Summer_E-Xperiance_2021_task
mvnw spring-boot:run
```
#### Testy
```
mvnw test
```

### Działanie
Dostęne są dwa endpointy dla metody GET:
* `/users/{user_name}/stars` : zwraca sumę gwiazdek w repozytoriach użytkownika {user_name}.
* `/users/{user_name}/repos` : zwraca repozytoria publiczne użytkownika {user_name}, wraz z nazwą i liczbą gwiazdek. Domyślnie są wyświetlane wszytskie repozytoria.   Dostępna jest opcja paginacji przez parametryzację zapytania:
  * `per_page` : maksymalna liczba repozytorów. Minimum 1, maksymalnie 100. W przypadku ustawienia innych parametrów domyślnie 30.
  * `page` : strona wyniku. Minimum 1. W przypadku ustawienia innych parametrów domyślnie 1.  
  
  W przypadku złych parametrów dostaniemy "Bad Request".
 
### Propozycje Rozszerzeń
  * dodanie możliwości autentyfikacji użytkownika.
  * rozszerzenie aplikacji o inne dane.
  * gromadzenie statystyk o projektach np użycie języków do rozwiązania danego problemu.
  * Aplikacja mobilna i strona internetowa do prezentacji wyników.



