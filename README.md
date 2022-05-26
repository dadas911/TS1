# TS1
<h2>Šachy: David Jansa</h2>

<h4>Popis programu</h4>
Jako téma mé semestrální práce jsem si vybral šachy, které budu vyhotovovat sám. Bude se jednat o standartní verzi šachů. Hra se bude ovládat přes vytvořené GUI, které bude přehledné a zároveň jednoduché a bude obsahovat klasické ikony pro jednotlivé figurky. Hra bude kontrolovat platnost tahů (např. rošáda). Rozehranou partii lze uložit do standartního formátu pro šachy - PGN. Z tohoto fotmátu lze i načítat hry a pokračovat v jejich hraní (pokud již nebyl checkmate).

<br>**Hlavní vlastnosti šachů**
- Vytvořená AI - ta náhodně hraje platné tahy
- Možnost uložení a následného načtení rozehrané partie ve formátu PGN
- Lze hrát hráč proti hráči, či hráč proti počítači

<h4>Uživatelský manuál</h4>

**Před prvním tahem**

Když zapnete program, jako první se objeví okno, do kterého zadáte jméno bílého a černého hráče + název souboru. Tento název bude použit při ukládání partie. Pokud jedno z jmen necháte prázdné, vytvoří se automaticky počítačem ovládaný hráč této strany. Po stisknutí "ok" se objeví šachovnice a můžete udělat první tah.

**Rozhraní**

V horní části obrazovky je menu s pěti možnostmi
<ul>
<li>Reset Game - Resetuje hru, přidá +1 score oboum z hráčů</li>
<li>Forfeit White - Bílý hráč se vzdává, hra se resetuje a bod se přidá černému hráči</li>
<li>Forfeit Black - Černý hráč se vzdává, hra se resetuje a bod se přidá bílému hráči</li>
<li>Load Game - Slouží pro načtení hry. Po kliknutí se objeví okno, do kterého zadáte jméno PGN souboru, který chcete načíst. Tento soubor musí být umístěn ve složce "gamesToLoad" (složka by tam po stáhnutí z GITu měla být).</li>
<li>Save Game - Uloží rozehranou partii do složky "savedGames", která se sama vytvoří, pokud již neexistuje. Hry, které skončí "checkmatem" se ukládájí automaticky </li>
</ul>

  
Dále je zde jméno hráče, jeho score a čas, který mu zbývá. Pokud tento čas vyprší, hráč prohrál. Střed celé herní plochy tvoří šachovnice a s ní pojmenování sloupců a řádek

**Hra**

Začíná bílý hráč, kterému se začne počítat čas až po odehrání prvního tahu. Tah se zahraje kliknutím na figurku a následným vybráním cílového políčka. Pokud je tah neplatný, je na řadě druhý hráč, nebo stav hry je "check" či "checkmate", hra hráče upozorní "popup" oknem. Druhý na tahu je černý hráč, kterému se čas spustí hned. Po skončení hry - checkmate/stalemate/forfeit se přidá bod patřičnému hráči a partie se resetuje na "začátek".
