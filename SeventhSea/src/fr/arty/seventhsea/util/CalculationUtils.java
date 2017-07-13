package fr.arty.seventhsea.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import fr.arty.seventhsea.entity.DicesSet;
import fr.arty.seventhsea.entity.Raise;
import fr.arty.seventhsea.type.RaiseType;

/**
 * Classe utilitaire pour le test.
 * 
 * @author Arty
 *
 */
public class CalculationUtils {

	public static final int TEN = 10;

	public static final int FIFTEEN = 15;

	// Supprime le constructeur par défaut.
	// Non instanciable.
	private CalculationUtils() {
	}

	/**
	 * Tri les combinaisons par valeur et pour une même valeurs par plus petit dés.
	 * 
	 * @param combinations
	 *            Liste des mises.
	 * @return la liste triée.
	 */
	public static List<Raise> sortRaises(List<Raise> combinations) {
		Collections.sort(combinations, new Comparator<Raise>() {
			@Override
			public int compare(Raise raise1, Raise raise2) {
				int pos = raise1.total() - raise2.total();
				if (pos == 0 && raise1.total() > 0) {
					int i = 0;
					while (pos == 0 && i < raise1.getDices().size()) {
						pos = raise1.getDices().get(i) - raise2.getDices().get(i);
						i++;
					}
				}

				return pos < 0 ? -1 : (pos > 0 ? 1 : 0);
			}
		});
		return combinations;
	}

	/**
	 * Génére aléatoirement nb valeurs représentant des dés de 10.
	 * 
	 * @param nb
	 *            nombre de dés de dix faces à générer.
	 * @return le nombre de dés.
	 */
	public static List<Integer> generateRandomDiceSet(int nb) {
		List<Integer> ret = new ArrayList<>(nb);
		for (int i = 0; i < nb; i++) {
			Random rand = new Random();
			ret.add(rand.nextInt(10) + 1);
		}
		return ret;
	}

	/**
	 * 
	 * @param dices
	 * @return
	 */
	public static Raise getARaise(DicesSet dices) {
		Raise raise = null;
		boolean betFound = false;

		// gestion des 10 : C'est easy, c'est traité à part. A noter que ça tombe à
		// l'eau pour les mises évoluées (rang 3)
		if (RaiseType.BASIC.equals(dices.getType()) && dices.getDicesMap().get(10) > 0) {
			raise = new Raise();
			raise.getDices().add(10);
			dices.removeRaiseDices(raise);
			betFound = true;
		}

		// autres cas
		if (!betFound) {
			// Au pire one ne pourra jamais utiliser plus de 10 dés pour faire
			// un 10.
			int diceDepth = 2;
			while (diceDepth <= 10 && !betFound) {
				BigDecimal dephtMoy = new BigDecimal(10).divide(new BigDecimal(diceDepth), 4, BigDecimal.ROUND_UP);
				if (dices.usufulDicesAverage().compareTo(dephtMoy) >= 0) {
					List<Raise> combinations = dices.combinations(diceDepth);
					if (combinations != null && combinations.size() > 0) {
						raise = combinations.get(0);
						dices.removeRaiseDices(combinations.get(0));
						betFound = true;
					} else {
						diceDepth++;
					}
				} else {
					diceDepth++;
				}
			}
		}
		return raise;
	}

}
