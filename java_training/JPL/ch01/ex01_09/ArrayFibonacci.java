package ex01_09;

public class ArrayFibonacci {

	/**
	 * Define title
	 */
	static final String title = "Fibonacci!";

	/**
	 * @param args
	 *
	 * 値が50未満のフィボナッチ数列を表示する
	 */
	public static void main(String[] args) {
		// Define Title
		// Show Title
		System.out.println(title);

		int lo = 1;
		int hi = 1;
		int[] result = new int[10];
		int count = 0;
		// System.out.println(lo);
		result[count++] = lo;
		while (hi < 50)
		{
			// System.out.println(hi);
			result[count++] = hi;
			hi = lo + hi;	// 新しいhi
			lo = hi - lo;	// 新しいloは、（合計 - 古いlo）
							// すなわち、古いhi
		}
		for (int i = 0; i < count; i++)
		{
			System.out.println(result[i]);
		}

	}

}