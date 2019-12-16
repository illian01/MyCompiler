int global_var;
int global_array[3];

int fibo(int n)
{
   if(n == 0) return 1;
   if(n == 1) return 1;
   return fibo(n-1) + fibo(n-2);
}

int sumFromZero(int n) 
{
   int i = 0;
   int sum = 0;
   while(i < n) 
   {
      sum = sum + i;
      i = i + 1;
   }
   
   return sum;
}

int arraySum(int arr[], int size)
{
   int i = 0;
   int sum = 0;
   
   while(i < size)
   {
      sum = sum + arr[i];
      i = i + 1;
   }
   
   return sum;
}

void main() 
{
   int i = 0;
   int local_var;
   int local_var_init = 10;
   int local_array[6];
   
   local_array[0] = 1;
   local_array[1] = 2;
   local_array[2] = 3;
   local_array[3] = 4;
   local_array[4] = 5;
   local_array[5] = 6;
   
   local_var = local_array[0];
   printf("1 : local_var = %d\n", local_var);
   
   local_var = local_var_init;
   printf("2 : local_var = %d\n", local_var);
   
   printf("---------------------------------------------\n");
   while(i < 6) {
   printf("%d  : local_array[%d] = %d\n", i + 3, i, local_array[i]);
   i = i+1;
   }
   printf("-------------------------------------------\n");
   
   local_array[0] = 7;
   local_array[1] = 8;
   local_array[2] = 9;
   local_array[3] = 10;
   local_array[4] = 11;
   local_array[5] = 12;
   
   i = 0;
   printf("--------------------------------------------\n");
   while(i < 6) {
   printf("%d : local_array[%d] = %d\n", i + 9, i, local_array[i]);
   i = i+1;}
   printf("--------------------------------------------\n");
   
   printf("15 : local_array sum = %d\n", arraySum(local_array, 6));
   
   global_array[0] = 100;
   global_array[1] = 200;
   global_array[2] = 300;
   
   printf("16 : fibo(20) = %d\n", fibo(20));
   
   printf("-end-\n");
}

