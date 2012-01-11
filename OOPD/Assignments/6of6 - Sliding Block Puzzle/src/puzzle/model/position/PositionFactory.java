package puzzle.model.position;

import puzzle.model.BoardModel;

public class PositionFactory
{
    private PositionFactory()
    {
        // no one should be creating instances of this class.
    }

    public static Position positionOfEmptySlot(BoardModel boardModel)
    {
        int positionOfActiveBlock = boardModel.getPositionOfEmptySlot();
        int rootOfSize = boardModel.getNumberOfSlotsInARow();
        int size = boardModel.getNumberOfSlots();

        int topRightCorner = rootOfSize - 1;
        int bottomLeftCorner = size - rootOfSize;
        int bottomRightCorner = size - 1;

        if (positionOfActiveBlock == 0)
            return TopLeftCorner.getInstance();
        if (positionOfActiveBlock < topRightCorner)
            return TopEdge.getInstance();
        if (positionOfActiveBlock == topRightCorner)
            return TopRightCorner.getInstance();

        if (positionOfActiveBlock < bottomLeftCorner)
        {
            int modulo = positionOfActiveBlock % rootOfSize;
            if (modulo == 0)
                return LeftEdge.getInstance();
            if (modulo == rootOfSize - 1)
                return RightEdge.getInstance();
            return Middle.getInstance();
        }

        if (positionOfActiveBlock == bottomLeftCorner)
            return BottomLeftCorner.getInstance();
        if (positionOfActiveBlock == bottomRightCorner)
            return BottomRightCorner.getInstance();
        return BottomEdge.getInstance();
    }
}