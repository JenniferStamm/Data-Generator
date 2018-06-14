package de.datageneration.generator;

import de.datageneration.generator.placement.structures.FunctionalDependencies;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.metanome.algorithms.hyfd.structures.FDTree;
import de.metanome.algorithms.hyfd.structures.FDTreeElement;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.lucene.util.OpenBitSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Function;

public class Util {
    public static void getSpecializations(FDTree root, OpenBitSet lhs, int rhs, List<OpenBitSet> foundLhs) {
        int currentLhsAttr = lhs.nextSetBit(0);
        OpenBitSet elementLhs = new OpenBitSet(root.getNumAttributes());
        getSpecializations(root, elementLhs, lhs, rhs, currentLhsAttr, foundLhs);
    }

    private static void getSpecializations(FDTreeElement element, OpenBitSet elementLhs, OpenBitSet lhs, int rhs, int currentLhsAttr, List<OpenBitSet> foundLhs) {
        if (!element.hasRhsAttribute(rhs)) {
            return;
        }

        if (currentLhsAttr < 0) {
            // All lhs elements contained
            if (element.isFd(rhs))
                foundLhs.add(elementLhs.clone());
        }

        if (element.getChildren() == null) {
            return;
        } else {
            for(int child = 0; child < element.getNumAttributes(); ++child) {
                if (element.getChildren()[child] != null) {
                    elementLhs.set(child);
                    if (child == currentLhsAttr)
                        getSpecializations(element.getChildren()[child], elementLhs, lhs, rhs, lhs.nextSetBit(currentLhsAttr + 1), foundLhs);
                    else
                        getSpecializations(element.getChildren()[child], elementLhs, lhs, rhs, currentLhsAttr, foundLhs);
                    elementLhs.clear(child);
                }
            }
        }
    }


    public static void forEachFDdo(FDTree tree, Function<FunctionalDependency, Void> func) {
        OpenBitSet lhs = new OpenBitSet(tree.getNumAttributes());
        forEachFDdo(tree, func, lhs);
    }

    private static void forEachFDdo(FDTreeElement element, Function<FunctionalDependency, Void> func, OpenBitSet lhs) {
        // Own FDs
        for (int i = element.getFds().nextSetBit(0); i >= 0; i = element.getFds().nextSetBit(i + 1)) {
            func.apply(new FunctionalDependency(lhs, i));
        }

        // Child FDs
        if (element.getChildren() == null) {
            return;
        } else {
            for(int childAttr = 0; childAttr < element.getNumAttributes(); ++childAttr) {
                FDTreeElement child = element.getChildren()[childAttr];
                if (child != null) {
                    lhs.set((long)childAttr);
                    forEachFDdo(child, func, lhs);
                    lhs.clear((long)childAttr);
                }
            }
        }
    }

    public static void forEachLHSdo(FDTree tree, Function<FunctionalDependencies, Void> func) {
        OpenBitSet lhs = new OpenBitSet(tree.getNumAttributes());
        forEachLHSdo(tree, func, lhs);
    }

    private static void forEachLHSdo(FDTreeElement element, Function<FunctionalDependencies, Void> func, OpenBitSet lhs) {
        // Own FDs
        if (element.getFds().cardinality() > 0) {
            func.apply(new FunctionalDependencies(lhs, element.getFds()));
        }


        // Child FDs
        if (element.getChildren() == null) {
            return;
        } else {
            for(int childAttr = 0; childAttr < element.getNumAttributes(); ++childAttr) {
                FDTreeElement child = element.getChildren()[childAttr];
                if (child != null) {
                    lhs.set((long)childAttr);
                    forEachLHSdo(child, func, lhs);
                    lhs.clear((long)childAttr);
                }
            }
        }
    }

    public static void writeFDTreeToFile(FDTree fdTree, File outputFile) {
        outputFile.getParentFile().mkdirs();
        try {
            Writer writer = new FileWriter(outputFile);

            try {
                writer.append(writeFDTree(fdTree));
            } catch (IOException e) {
                e.printStackTrace();
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String writeFDTree(FDTree fdTree) {
        StringBuilder stringBuilder = new StringBuilder();
        forEachLHSdo(fdTree, (nonFD) -> {
            OpenBitSet lhs = nonFD.getLhs();
            OpenBitSet rhs = nonFD.getRhs();

            stringBuilder.append(openBitSetToString(lhs));
            stringBuilder.append(" -> ");
            stringBuilder.append(openBitSetToString(rhs));

            stringBuilder.append("\n");
            return null;
        });
        return stringBuilder.toString();
    }

    public static String openBitSetToString(OpenBitSet a) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean firstAttribute = true;
        for (int i = a.nextSetBit(0); i >= 0; i = a.nextSetBit(i + 1)) {
            if (!firstAttribute) {
                stringBuilder.append(",");
            }
            firstAttribute = false;
            stringBuilder.append("A");
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    public static OpenBitSet getCompleteRHS(FDTreeElement root, OpenBitSet lhs) {
        FDTreeElement element = root;
        for (int i = lhs.nextSetBit(0); i >= 0; i = lhs.nextSetBit(i + 1)) {
            //if ((element.getChildren() == null) || (element.getChildren()[i] == null))
            //    return false;
            element = element.getChildren()[i];
        }
        return element.getFds();
    }


    public static boolean contains(OpenBitSet a, OpenBitSet b) {
        OpenBitSet and = a.clone();
        and.and(b);
        if (and.equals(b)) {
            return true;
        } else {
            return false;
        }
    }

    public static OpenBitSet findContained(OpenBitSet a, OpenBitSet b) {
        OpenBitSet result = a.clone();
        result.and(b);
        return result;
    }

    public static boolean isTrivial(OpenBitSet lhs, Integer rhs) {
        return lhs.get(rhs);
    }


    public static long numFDsInLevel(int nCols, int level) {
        return CombinatoricsUtils.binomialCoefficient(nCols, level) * (nCols - level);
    }

    public static Set<OpenBitSet> getDirectSupersets(OpenBitSet bitSet, int nCols) {
        Set<OpenBitSet> supersets = new HashSet<>();
        for (int i = 0; i < nCols; i++) {
            if (!bitSet.get(i)) {
                OpenBitSet superset = bitSet.clone();
                superset.set(i);
                supersets.add(superset);
            }
        }
        return supersets;
    }

    // X -> y and Y -> Z => X -> Z
    public static FunctionalDependencies findTransitives(FunctionalDependencies leftFD, FunctionalDependencies rightFD) {
        OpenBitSet leftLhs = leftFD.getLhs();
        OpenBitSet leftRhs = leftFD.getRhs();
        OpenBitSet rightLhs = rightFD.getLhs();
        OpenBitSet rightRhs = rightFD.getRhs();

        if (!leftRhs.equals(rightLhs)) {
            return null;
        }

        return new FunctionalDependencies(leftLhs, rightRhs);
    }

    // X -> Y and WY -> Z => WX -> Z
    public static FunctionalDependencies findPseudoTransitives(FunctionalDependencies leftFD, FunctionalDependencies rightFD) {
        OpenBitSet leftLhs = leftFD.getLhs();
        OpenBitSet leftRhs = leftFD.getRhs();
        OpenBitSet rightLhs = rightFD.getLhs();
        OpenBitSet rightRhs = rightFD.getRhs();

        // might be X -> AY where A is irrelevant
        OpenBitSet y = Util.findContained(rightLhs, leftRhs);
        if (y.isEmpty()) {
            return null;
        }

        OpenBitSet transitiveLhs = rightLhs.clone(); // WY
        transitiveLhs.remove(y); // remove Y
        transitiveLhs.or(leftLhs); // add X => WX

        return new FunctionalDependencies(transitiveLhs, rightRhs);
    }

    public static OpenBitSet getRandomGeneralization(OpenBitSet lhs) {
        Random rand = new Random();
        OpenBitSet result;
        result = lhs.clone();
        for (int i = lhs.nextSetBit(0); i >= 0; i = lhs.nextSetBit(i + 1)) {
            if (rand.nextBoolean()) {
                result.clear(i);
            }
        }
        return result;
    }

    public static FunctionalDependency randomFD(FDTree tree) {
        OpenBitSet lhs = new OpenBitSet(tree.getNumAttributes());
        return randomFD(tree, lhs);
    }

    private static FunctionalDependency randomFD(FDTreeElement element, OpenBitSet lhs) {
        Random rand = new Random();

        // Own FDs
        ArrayList<Integer> ownFDs = new ArrayList<>();
        for (int i = element.getFds().nextSetBit(0); i >= 0; i = element.getFds().nextSetBit(i + 1)) {
            ownFDs.add(i);
        }

        // Child FDs
        ArrayList<Integer> children = new ArrayList<>();
        if (element.getChildren() != null) {
            for(int childAttr = 0; childAttr < element.getNumAttributes(); ++childAttr) {
                FDTreeElement child = element.getChildren()[childAttr];
                if (child != null) {
                    children.add(childAttr);
                }
            }
        }
        boolean chooseOwnFD;
        if (children.isEmpty()) {
            chooseOwnFD = true;
        } else if (ownFDs.isEmpty()) {
            chooseOwnFD = false;
        } else {
            chooseOwnFD = rand.nextBoolean();
        }

        if (chooseOwnFD) {
            return new FunctionalDependency(lhs, ownFDs.get(rand.nextInt(ownFDs.size())));
        } else {
            int randomChildAttr = children.get(rand.nextInt(children.size()));
            lhs.set(randomChildAttr);
            return randomFD(element.getChildren()[randomChildAttr], lhs);
        }
    }
}
