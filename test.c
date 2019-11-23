int func(int x, int y) 
{
	int z = 0;
	if(x == y)
	{
		z = 1;
	}
	
	return z;
}

int sum(int n) 
{
	int i = 0;
	int sum = 0;
	while(i <= n)
	{
		sum = sum + i;
		i = i + 1;
	}
	return sum;
}

void ppp()
{
	_print(11111);
	_print(1111);
	_print(111);
	_print(11);
	_print(1);
	return ;
}

void main () 
{
	int t = 100;  
	int n;
	n = 50;
	_print(func(1,1));
	_print(func(1,2));
	_print(sum(t));
	_print(0 or 1);
	ppp();
	return ;
}
