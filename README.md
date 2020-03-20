---
output:
  pdf_document: default
  html_document: default
---
***
### Wstęp
Celem zadania było zaimplementowanie algorytmu obliczania odległości Levenshteina. 
Odległość ta to miara odmienności dwóch ciągów znaków, określana za pomocą liczby potrzebnych do wykonania
operacji prostych, aby przekształcić jeden łańcuch w drugi.  
Operacje proste:  
    - Dodanie znaku,  
    - Usunięcie znaku,  
    - Zamienienie znaku na inny.

Algorytm może być zastosowany do wyszukiwania tekstu, który mógł być niepoprawnie wprowadzony, na przykład podpowiadanie poprawnych wyrazów w telefonie.

### Implementacja algorytmu Levenshteina   

##### 1. Utworzenie macierzy dla algorytmu

Pierwszym krokiem jest utworzenie macierzy w której zapisywane będą wyliczone odległości 
oraz wypełnienie jej wartościami początkowymi.  Macierz ma rozmiary _m + 1_ x _n + 1_, gdzie _m_ to liczba znaków w wzorcowym wyrażeniu, 
a _n_ to liczba znaków w aktualnie porównywanym łańcuchu. Pierwszy wiersz macierzy zostaje wypełniony wartościami od 0 do _n_, pierwsza kolumna jest wypełniana wartościami od 0 do _m_.

```java
private int[][] initMatrix(int matrixRows, int matrixCols) {
        int[][] matrix = new int[matrixRows + 1][matrixCols + 1];
        for (int i = 0; i <= matrixRows; i++) matrix[i][0] = i;
        for (int i = 1; i <= matrixCols; i++) matrix[0][i] = i;
        return matrix;
    }

//Wywołanie towrzenia macierzy
int[][] distanceMatrix = initMatrix(pattern.length, expression.length);
```

##### 2. Porównanie wartości  
Wyliczenie kosztu dla całego wyrażenia składa się z wyliczenia kosztu dla każdej kombinacji liter, z porównywanych wyrażeń. Koszt operacji wynosi _1_ jeśli porównywane znaki są różne i _0_ jeśli są identyczne. Następnie uzupełniana jest komórka macierzy, na przecięciu porównywanych znaków, wartością najmniejszą wybraną z następujących:  
- Wartość komórki powyżej z macierzy + 1,  
- Wartość komórki po lewej + 1,  
- Wartość komórki położonej jedno pole wyżej i jedno pole w lewo + wyliczony koszt.  
Algorytm powtarzamy do wypełnienia całej macierzy.

```java
int cost;
for (int i = 1; i <= pattern.length; i++) {
    for (int j = 1; j <= expression.length; j++) {
        cost = calculateCost(pattern[i - 1], expression[j - 1]);
        distanceMatrix[i][j] = pickMinimum(distanceMatrix[i - 1][j] + 1,
                distanceMatrix[i][j - 1] + 1,
                distanceMatrix[i - 1][j - 1] + cost);
    }
}

//Metoda wyliczająca koszt
private int calculateCost(char a, char b) {
    return a == b ? 0 : 1;
}
```

##### 3. Wynik algorytmu
Wynikiem algorytmu jest ostatnia komórka ostatniego wiersza wypełnionej macierzy.
```java
//Przykład wypełnionej macierzy
     A B A B A
   0 1 2 3 4 5 
A  1 0 1 2 3 4 
A  2 1 1 1 2 3 
A  3 2 2 1 2 2 
```

### Wczytanie danych do programu
Program oczekuje dwóch argumentów, pierwszym jest nazwa pliku, z którego mają być wczytane dane, drugim jest wyrażenie wzorcowe.
```java
public static void main(String[] args) {
    String fileName;
    String pattern;
    int bestMatchLineNumber;

    try {
        fileName = args[0];
        pattern = args[1];
        bestMatchLineNumber = findBestMatchingLineInFile(fileName, pattern);
        System.out.println(bestMatchLineNumber);
    } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Missing parameters");
        e.printStackTrace();
    }
}
```

Metoda otwierająca plik i znajdująca w nim linię, która najlepiej odpowiada podanemu wzorcowi.
```java
private static int findBestMatchingLineInFile(String fileName, String pattern) {
    Levenshtein levenshtein = new Levenshtein(pattern);

    int lineCounter = 1;
    int bestMatchLineNumber = 0;
    int currentLineMatchValue;
    int bestMatchValue = Integer.MAX_VALUE;

    try {
        FileInputStream inputStream = new FileInputStream(fileName);
        Scanner myReader = new Scanner(inputStream);
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            currentLineMatchValue = levenshtein.compareWithPattern(line);
            if (currentLineMatchValue < bestMatchValue) {
                bestMatchLineNumber = lineCounter;
                bestMatchValue = currentLineMatchValue;
            }
            if (bestMatchValue == 0) break; //Przerwanie porównywania jeśli aktualna linia 
                                            //pokrywa się w pełni z wzorcem
            lineCounter++;
        }
        myReader.close();
    } catch (FileNotFoundException e) {
        System.out.println("File not found");
        e.printStackTrace();
    }
    return bestMatchLineNumber;
}
```

