Метод умножения матриц относится к группе методов шифрования аналитическими преобразованиями. В данном шифре используется метод алгебры матриц: умножение двух матриц: K x M = E
Если использовать матрицу K в качестве ключа, а вместо вектора M подставить набор числовых представлений символов открытого сообщения, то компоненты вектора E будут содержать зашифрованные значения переданного сообщения.
Чтобы получить открытый текст M из закрытого сообщения E достаточно провести обратную операцию (умножение на обратную матрицу K^-1 ключа матрицы): K^-1 x E = M
Чтобы иметь возможность расшифровать сообщение E при помощи ключа K, ключ матрица K должна быть квадратной и с определителем, не равной нулю. В противном случае матрица K не будет иметь обратную форму K^-1 и расшифровка сообщения E будет невозможной.
Обратная матрица K^-1 вычисляется при помощи нахождения присоединенной матрицы и делением всех её элементов на определитель.
Присоединенная матрица - матрица той же размерности n, элементы которой: алгебраические дополнения: A_ij = (-1)^(i+j) x D_ij, где D_ij - определитель матрицы, получаемый вычеркиванием i-й строки и j-го столбца матрицы A_ij.
Наконец полученный результат деления элементов присоединенной матрицы на определитель исходной необходимо транспонировать, после чего мы получим обратную матрицу K^-1 для расшифровки.
Если открытое сообщение по своей длине больше ранга n матрицы ключа K, то шифровка происходит отдельно для каждого вектора M из n символов сообщения.
Если открытое сообщение не делится нацело на ранг n, то на конец сообщения добавляются пробельные символы «_» пока остаток деления длины сообщения на ранг не станет равной нулю.