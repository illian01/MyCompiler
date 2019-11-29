int global_var;
int global_array[3];
void main() 
{
	int local_var_init = 10;
	global_var = local_var_init;
	print_d(global_var);
	global_var = global_var + 20;
	global_array[1] = global_var;
	print_d(global_array[1]);
	local_var_init = 2+global_array[1]+2;
	global_array[1] = local_var_init+2;
	print_d(global_array[1]);
	global_array[1] = 2+local_var_init+2;
	print_d(global_array[1]);
}
