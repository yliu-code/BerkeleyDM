package bearmaps.utils.ps;

import java.util.List;

public class KDTree implements PointSet {

    private KDTreeNode root;
    private double best;
    private KDTreeNode bestNode;

    /* Constructs a KDTree using POINTS. You can assume POINTS contains at least one
       Point object. */
    public KDTree(List<Point> points) {
        boolean xDim = true;
        root = new KDTreeNode(points.get(0));
        for (Point point : points) {
            if (point != root.point) {
                insert(root, point, xDim);
            }
        }

    }

    /*

    You might find this insert helper method useful when constructing your KDTree!
    Think of what arguments you might want insert to take in. If you need
    inspiration, take a look at how we do BST insertion!

    */

    private void insert(KDTreeNode node, Point point, Boolean xDim) {
        if (xDim) {
            if (point.getX() < node.point.getX()) {
                if (node.left == null) {
                    node.left = new KDTreeNode(point);
                    return;
                } else {
                    insert(node.left, point, !xDim);
                }
            } else {
                if (node.right == null) {
                    node.right = new KDTreeNode(point);
                    return;
                } else {
                    insert(node.right, point, !xDim);
                }
            }
        } else {
            if (point.getY() < node.point.getY()) {
                if (node.left == null) {
                    node.left = new KDTreeNode(point);
                    return;
                } else {
                    insert(node.left, point, !xDim);
                }
            } else {
                if (node.right == null) {
                    node.right = new KDTreeNode(point);
                    return;
                } else {
                    insert(node.right, point, !xDim);
                }
            }
        }
    }

    /* Returns the closest Point to the inputted X and Y coordinates. This method
       should run in O(log N) time on average, where N is the number of POINTS. */
    public Point nearest(double x, double y) {
        best = Integer.MAX_VALUE;
        bestNode = root;
        nearestHelper(root, new Point(x, y), true);
        return bestNode.point;
    }

    private void nearestHelper(KDTreeNode node, Point point, boolean xDim) {

        boolean wentLeft = true;

        double currDistance = Point.distance(node.point, point);
        if (currDistance < best) {
            best = currDistance;
            bestNode = node;
        }

        if (xDim) {
            if (point.getX() < node.point.getX()) {
                if (node.left != null) {
                    nearestHelper(node.left, point, !xDim);
                }
            } else {
                wentLeft = false;
                if (node.right != null) {
                    nearestHelper(node.right, point, !xDim);
                }
            }
        } else {
            if (point.getY() < node.point.getY()) {
                if (node.left != null) {
                    nearestHelper(node.left, point, !xDim);
                }
            } else {
                wentLeft = false;
                if (node.right != null) {
                    nearestHelper(node.right, point, !xDim);
                }
            }
        }

        if (xDim) {
            if (Point.distance(point, new Point(node.point.getX(), point.getY())) < best) {
                if (!wentLeft) {
                    if (node.left != null) {
                        nearestHelper(node.left, point, !xDim);
                    }
                } else {
                    if (node.right != null) {
                        nearestHelper(node.right, point, !xDim);
                    }
                }
            }
        } else {
            if (Point.distance(point, new Point(point.getX(), node.point.getY())) < best) {
                if (!wentLeft) {
                    if (node.left != null) {
                        nearestHelper(node.left, point, !xDim);
                    }
                } else {
                    if (node.right != null) {
                        nearestHelper(node.right, point, !xDim);
                    }
                }
            }
        }

    }

    private class KDTreeNode {

        private Point point;
        private KDTreeNode left;
        private KDTreeNode right;

        // If you want to add any more instance variables, put them here!

        KDTreeNode(Point p) {
            this.point = p;
        }

        KDTreeNode(Point p, KDTreeNode left, KDTreeNode right) {
            this.point = p;
            this.left = left;
            this.right = right;
        }

        Point point() {
            return point;
        }

        KDTreeNode left() {
            return left;
        }

        KDTreeNode right() {
            return right;
        }

        // If you want to add any more methods, put them here!

    }
}
