import java.io.*;
import java.util.HashMap;

import com.microsoft.z3.*;

public class Sudoku {

	BoolExpr[][][] v;
	HashMap<String, String> cfg = new HashMap<String, String>();

	Solver solver;
	Context context;

	public Sudoku() {
		this.v = new BoolExpr[9][9][9];
		this.cfg = new HashMap<String, String>();
		cfg.put("model", "true");
		cfg.put("proof", "true");
		this.context = new Context(cfg);
		this.solver = this.context.mkSolver();

		// creation des 9^3 variables booleenes du problème
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				for (int k = 0; k <= 8; k++) {
					v[i][j][k] = context.mkBoolConst("v" + "_" + i + "_" + j + "_" + k);
				}
			}
		}

		// une case ne peut prendre qu'une valeur
		try {
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				for (int k = 0; k <= 7; k++) {
					for (int p = k + 1; p <= 8; p++) {
						solver.add(context.mkOr(context.mkNot(v[i][j][k]), context.mkNot(v[i][j][p])));
					}
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// une case contient forcément une valeur
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				solver.add(context.mkOr(v[i][j]));
			}
		}

		// une ligne ne peut pas contenir 2 fois le même chiffre
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				for (int k = 0; k <= 7; k++) {
					for (int p = k + 1; p <= 8; p++) {
						solver.add(context.mkOr(context.mkNot(v[i][k][j]), context.mkNot(v[i][p][j])));
					}
				}
			}
		}

		// une colonne ne peut pas contenir 2 fois le même chiffre
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				for (int k = 0; k <= 7; k++) {
					for (int p = k + 1; p <= 8; p++) {
						solver.add(context.mkOr(context.mkNot(v[k][i][j]), context.mkNot(v[p][i][j])));
					}
				}
			}
		}

		// chaque 'bloc' 3x3 doit contenir les chiffres de 1 à 9
		// [0][0] ... [0][2] ... [0][3] ... [0][5] ... [0][6] ... [0][8]
		// . .
		// [2][0] ... [2][2] ... [2][3] ... [2][5] ... [2][6] ... [2][8]
		//
		//
		//
		// [3][0] ... [3][2] ... [3][3] ... [3][5] ... [3][6] ... [3][8]
		// . .
		// [5][0] ... [5][2] ... [5][3] ... [5][5] ... [5][6] ... [5][8]
		//
		//
		//
		// [6][0] ... [6][2] ... [6][3] ... [6][5] ... [6][6] ... [6][8]
		// . .
		// [8][0] ... [8][2] ... [8][3] ... [8][5] ... [8][6] ... [8][8]

		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				for (int k = 0; k <= 7; k++) {
					for (int p = k + 1; p <= 8; p++) {
						for (int l = 0; l <= 8; l++) {
							solver.add(context.mkOr(context.mkNot(v[3 * i + k / 3][3 * j + k % 3][l]),
									context.mkNot(v[3 * i + p / 3][3 * j + p % 3][l])));
						}
					}
				}
			}
		}

	}


	public void loadSudoku(String filename, boolean logEnabled) throws IOException {
		
		
		// On initialise le Sudoku avec toutes ses contraintes
		//Sudoku sudoku = new Sudoku();
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		// first line contains dimension
		String line = br.readLine();
		int n = Integer.parseInt(line);

		// parse each line
		int i = 0;

		while ((line = br.readLine()) != null) {
			String values[] = line.split(",");

			for (int j = 0; j < values.length; j++) {
				if (!values[j].equals("")) {
					System.out.println("found value " + values[j] + " at position (" + i + ", " + j + ")");
					// you should add the value in your Sudoku here!
					int val = Integer.parseInt(values[j]);
					this.solver.add(context.mkAnd(v[i][j][val-1], v[i][j][val-1]));
					System.out.println();
				}
			}
			i++;
		}

		br.close();

		Sudoku.checkAndPrint(this.solver);
	}
	
	
    static Model check(Solver solver) {
        if (solver.check() == Status.SATISFIABLE) {
            return solver.getModel();
        } else {
            return null;
        }
    }

    static void checkAndPrint(Solver solver) {
    	
		// print the complexity of the problem 
		System.out.println("\n\n*** NumAssertions : " + solver.getNumAssertions());

		System.out.println(solver.getStatistics());
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
        Status q = solver.check();

        switch (q) {
        case UNKNOWN:
            System.out.println("  Unknown because:\n" +
                               solver.getReasonUnknown());
            break;
        case SATISFIABLE:
            System.out.println("  SAT, model:\n" +
                               solver.getModel());
            break;
        case UNSATISFIABLE:
            System.out.println("  UNSAT, proof:\n" +
                               solver.getProof());
            break;
        }
    }
	
	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out.println("" + "\n#############################################\n" + " Usage : java Sudoku.java file_path\n"
					+ " Exemple : java Sudoku.java files/le-monde.csv" + "\n#############################################\n");
			System.exit(1);
		}
		String filePath = args[0];
		
		Sudoku sudoku = new Sudoku();
		try {
			sudoku.loadSudoku(filePath, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}