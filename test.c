int global_var;
int global_array[3];
void main() 
{
   int local_var;
   int local_var_init = 10;
   int local_array[6];
   
   local_array[0] = local_var_init;
   local_array[1] = 0;
   local_array[2] = 0;
   local_array[3] = 0;
   local_array[4] = 0;
   local_array[5] = 0;
   
   local_var = local_array[0];
   
   global_array[0] = 0;
   global_array[1] = 0;
   global_array[2] = 0;
   
   printf("%d %d", local_var, local_array[1]);
   printf("%s\t\n%d ~ 35[2]", "abc");
}