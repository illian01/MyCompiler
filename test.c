int global_var = 5;

void main() 
{
	int a = 3;
	int b[5];
	b[1]=3;
	a = global_var+4+b[1];
	print_d(a);	
}

