# 1. Struktura Projektu

Projekt składa się z:

- Controllerów: Odpowiadają za obsługę Rest Api.
- Modeli: Zawierają klasy reprezentujące obrazy i miniatury
- Service'y: Implementują główną logike aplikacji, w tym operacje na obrazach i miniaturach.
- Queue: Zawiera klasy do obsługi kolejki zadań, umożliwiając asynchroniczne przetwarzanie obrazów.
- Repository: Odpowiada za zapytania do bazy MongoDB.

## Schemat Backendu
![!\[Alt text\](Diagram.jpg)](diagrams/Diagram.jpg)

# 2. Baza Danych

Projekt korzysta z bazy danych MongoDB do przechowywania informacji o obrazach oraz miniaturach. Klasy ImageRepository i ThumbnailRepository realizują operacje na bazie danych.
# Schemat bazy danych
![!\[Alt text\](DatabseDiagram.jpg)](diagrams/DatabseDiagram.jpg)

# 3. Milestone 1
Mechanizm Kolejkowania Obrazów

    Implementacja klasy TaskQueue umożliwiającej kolejkowanie obrazów do przetwarzania asynchronicznego.

Wysyłanie Obrazów do Przetworzenia

    Implementacja odpowiednich metod w ImageController obsługujących wysyłanie  obrazów na serwer.

Przetwarzanie obrazów

    Implementacja metody odpowiedzialnej za przetwarzanie obrazów w miniatury.

Możliwość doczytywania miniatur gdy będą już gotowe do wyświetlenia

    Implementacja odpowiednich metod w ImageController i ThumbnailController.

# 4. Milestone 2
Obsługa Miniatur w 3 Rozmiarach

    Dodanie do GUI funkcji przełączania rozmiaru miniatur.

Odporność na Awarie Serwera

    Zaimplementowano mechanizm, który gwarantuje, że niepoliczone miniatury zawsze w końcu zostaną policzone, nawet w przypadku awarii serwera.

GUI do Obsługi Aplikacji

    Każda miniatura pojawia się tak szybko, jak to możliwe, z wykorzystaniem placeholderów do doczytywania.
    Dodanie funkcji uploadu obrazów z poziomu UI.
    Możliwość podglądu obrazu w oryginalnym rozmiarze po kliknięciu w miniaturę.
