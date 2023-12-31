Криптосистема Меркла-Хеллмана основана на задаче суммы подмножеств и является продолжением мысли метода укладкой ранца.

В данной системе используется два ключа:
- Закрытый ключ: (W, q, r');
- Открытый ключ: B.

Система и её ключи создаётся по следующему алгоритму:
- Выбирается размер одного передаваемого блока в битах n:
	- Очевидно выбранный размер n должен быть выбран так, чтобы для представления всех возможных сообщений не требовалось больше чем 2^n различных двоичных чисел.
- Случайно выбирается сверх возрастающий набор из n положительных целых чисел: W = (w_0, w_1, ..., w_{n-1}):
	- Под сверх возрастающим набором понимается такой набор, для каждого элемента которого верно следующее неравенство: w_k > sum^{k-1}_{i=0}{w_i} : 0 <= k <= n-1 (каждый элемент больше суммы всех предыдущих).
- Выбирается случайное простое число q, для которого верно неравенство: q > sum^{n-1}_{i=0}{w_i} (больше суммы всех элементов в наборе);
- Выбирается случайное простое число r, являющееся взаимно простым числу q (НОД(r, q) = 1);
- Вычисляется набор значений B = (b_0, b_1,...,b_{n-1}) по формуле: b_{i} = (rw_i) % q, i в диапазоне (0, n-1);
- Вычисляется мультипликативное обратное значение r' для r:
	- Мультипликативное обратное число: то число r', для которого верно утверждение: (r' *  r) % q = 1;
	- Мультипликативное обратное вычисляется при помощи расширенного алгоритма Евклида, представленного далее.

Алгоритм вычисления мультипликативное обратное число r' для r по модулю q при помощи расширенного алгоритма Евклида:
- Пусть r  примерно равно  a и r'  примерно равно  n;
- Определим: t = 0, newT = 1, r = n, newR = a;
- Пока newR != 0 выполняем следующие операции:
	- Пусть quotient = t / newR - целая часть деления t на newR;
	- tempT = t;
	- t = newT;
	- newT = tempT - quotient * newT;
	- tempR = r;
	- r = newR;
	- newR = tempR - quotient * newR.
- Если после выполнения цикла выше r > 1, то не существует мультипликативного обратного числа r' для r;
- Иначе, если t < 0, то t = t + n;
- t - мультипликативное обратное число r' для r.

В результате выполнения алгоритма выше мы получаем:
- Закрытый ключ: (W, q, r');
- Открытый ключ: B.

Зашифровка сообщений происходит по следующей схеме:
- Дано сообщение M, которое можно разделить на N числовых блоков: M=(M_0,M_1,...,M_{N-1}) размером в n бит: M_i = (m_{i0}, m_{i1}, ..., m_{in}), где m_0 - бит с наивысшим порядком;
- Известен открытый ключ B = (b_0, b_1,...,b_{n-1});
- Вычисляется зашифрованное сообщение C = (C_0, C_1, ..., C_{N-1}) по формуле: c_i = sum^{n-1}_{j=0}{m_{ij} *  b_j}, i в диапазоне (0, N-1) (сумма по побитовому и между M_i и B).
В итоге мы получаем зашифрованное сообщение C = (C_0, C_1, ..., C_{N-1}).

Алгоритм расшифровки закрытого сообщения:
- Пусть дано зашифрованное числовое сообщение размером N: C = (C_0, C_1, ..., C_{N-1});
- Известен закрытый ключ (W, q, r');
- Вычисляем значение набора C' = (c'_0, c'_1,...,c'_{N-1}) по формуле c'_i = (c_i *  r') % q;
- Решаем проблему нахождения сумм C' подмножества по индексам X_i, i в диапазоне (0,N-1) набора W:
	- Иными словами, для каждого числового значения c'_i, i в диапазоне (0,N-1) находим соответствующий набор индексов X_i = (x_{i0}, x_{i1},...,x_{ik}), которые дают в сумме c'_i при применении на W: c'_i = sum^{k-1}_{j = 0}{w_{x_{ij}}}, где k  в диапазоне  (1, n) - произвольное количество необходимых индексов;
	- Алгоритм решения задачи нахождения индексов представлено далее.
- Из полученных индексов строятся открытые блоки m_i при помощи подстановки значения 1 в соответствующие по X_i индексы двоичного представления:
	- Иными словами: M_i = sum^{k-1}_{j=0}{2^{n - x_{ij} - 1}} (n используется из-за перевёрнутого двоичного представления).
В итоге остаётся расшифрованное сообщение M = (M_0,M_1,...,M_{N-1}).

Алгоритм решения задачи сумм подмножества множества и их индексов:
- Пусть дано c'_i, для которого необходимо найти индексы X_i=(x_{i0},x_{i1},...,x_{ik}), k в диапазоне (1,n) элементов множества W, в сумме дающие c'_i;
- Создаётся пустой список X_i;
- Выполняются следующие действия по циклу:
	- Находим наибольший элемент W, меньший или равный c'_i (обозначим его как w_j);
	- c'_i = c'_i - w_j;
	- Добавляем соответствующий индекс j в список X;
	- Если c'_i <= 0 то выходим из цикла.
- Если c'_i < 0, то не существует решения задачи нахождения суммы подмножества множества;
- Иначе: список X соответствует списку, решающий задачу нахождения суммы X = (x_0,x_1,...,x_k) = X_i: sum^{k-1}_{j=0}{w_{x_j}} = c'_i.