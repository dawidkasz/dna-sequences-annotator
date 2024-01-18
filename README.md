# System do adnotacji sekwencji DNA

## Cel projektu

Celem projektu jest stworzenie platformy do wspomagania adnotacji
wariantów splicingowych, która pozwoli na efektywne przetwarzanie i analizę
wariantów genetycznych, zwłaszcza tych, które potencjalnie zaburzają proces
splicingu. System ma być w stanie przetwarzać warianty typu SNV
dla całego genomu, korzystając z kilku wybranych algorytmów, oraz umożliwiać
przeliczenie online wariantów typu INDEL.

## Komponenty systemu

### annotator_ui

Prosty interfejs graficzny, który opcjonalnie może zostać wykorzystany do manualnego wgrywaniu wariantów do adnotacji.

### annotator_core

Główny komponent systemu udostępniający interfejs REST API.

Diagram klas
[](./docs/annotator_core.png)

### annotator_worker

Komponent systemu odpowiadający za efektywne przetwarzania danych genetycznych i wykonywanie algorytmów adnotacji sekwencji DNA.

Diagram klas
[](./docs/annotator_worker.png)