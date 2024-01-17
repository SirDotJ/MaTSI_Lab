Общая схема работы по шифру Тритемиуса:
- Выбирается некая связь k = f(p), где:
	- k - смещение буквы;
	- p - код буквы открытого алфавита;
	- f(p) - функция зависимости смещения k От кода p:
		- При этом для любых переданных значений p на выходе должно быть целое число, которое приводит к особому новому значению при сдвиге соответствующего символа алфавита.
- Для всех символов p_i, i в диапазоне (0,N-1) вычисляется соответствующее смещение k_i = f(p_i);
- Пусть теперь нам необходимо зашифровать сообщение M=(M_0,M_1,...,M_{n-1}) (n - размер сообщения в символах) при помощи вычисленных смещений k;
- Для генерации закрытого текста C = (C_0,C_1,...,C_{n-1}) используется формула: C_i = (M_i + k_i) % N, i в диапазоне (0,n-1):
	- При этом если вычисленное значение C_i - отрицательное число, то C_i = N - |C_i|.
- Для расшифровки текста C = (C_0,C_1,...,C_{n-1}) используется формула: M_i = (C_i - k_i) % N,i в диапазоне ((0,n-1):
	- При этом если вычисленное значение M_i - отрицательное число, то M_i = N - |M_i|.