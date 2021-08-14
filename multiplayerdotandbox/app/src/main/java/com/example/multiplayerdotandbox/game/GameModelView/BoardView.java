package com.example.multiplayerdotandbox.game.GameModelView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.multiplayerdotandbox.R;
import com.example.multiplayerdotandbox.game.controllerss.Game;
import com.example.multiplayerdotandbox.game.controllerss.Player;
import com.example.multiplayerdotandbox.game.controllerss.PlayerHuman;

import static com.example.multiplayerdotandbox.game.controllerss.Game.State.PLAYER1_TURN;

public class BoardView extends View {

    private Game game;
    private int horizontalOffset;
    private int verticalOffset;
    private int[][] boxesAlpha;

    private int boxSide;
    private int snapLength;
    private int dotRadius;

    private Paint linePaint;
    private Paint linePaint1;
    private Paint lineTempPaint;
    private Paint dotPaint;
    private Paint boxPaint;

    private float touchWidth;
    private float touchHeight;

    private int colorPlayer1;
    private int colorPlayer2;
    private int colorPlayer11;
    private int colorPlayer22;

    private Game.State gameState;

    private int boxColor;

    private float x1temp, y1temp, x2temp, y2temp;
    private boolean drawTemp = false;
    private boolean touchable = false;

    private boolean shouldMakeASound;
    private boolean isTurnMe;

    public BoardView(Context context) {
        super(context);
        init();
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_white));

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(18f);
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.brown3));


        linePaint1 = new Paint();
        linePaint1.setAntiAlias(true);
        linePaint1.setStrokeWidth(18f);
        linePaint1.setColor(ContextCompat.getColor(getContext(), R.color.brown1));

        dotPaint = new Paint();
        dotPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        dotPaint.setAntiAlias(true);

        boxPaint = new Paint();
        boxPaint.setAntiAlias(true);
        boxPaint.setColor(ContextCompat.getColor(getContext(), R.color.blue));

        boxSide = getResources().getDimensionPixelSize(R.dimen.default_box_size);
        snapLength = getResources().getDimensionPixelSize(R.dimen.default_snap_length);
        dotRadius = getResources().getDimensionPixelSize(R.dimen.dot_size);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        shouldMakeASound = sharedPref.getBoolean(getContext().getString(R.string.pref_key_sound), true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (game == null)
            return;

        Grid board = game.getBoard();

        boolean revalidate = false;
        int x1, y1, x2, y2;
        for (int i = 0; i < board.getRows(); i++)
            for (int j = 0; j < board.getColumns(); j++) {
                Box box = board.getBoxAt(i, j);

                if (!isTurnMe) {
                    if (box.player == Player.PLAYER2)
                        boxPaint.setColor(colorPlayer1);
                    else
                        boxPaint.setColor(colorPlayer2);
                } else {
                    if (box.player == Player.PLAYER2)
                        boxPaint.setColor(colorPlayer2);
                    else
                        boxPaint.setColor(colorPlayer1);
                }
                if (box.left && box.right && box.bottom && box.top) {

                    if (boxesAlpha[i][j] < 255) {
                        revalidate = true;
                        boxesAlpha[i][j] += 5;
                    }

                    boxPaint.setAlpha(boxesAlpha[i][j]);

                    x1 = horizontalOffset + j * boxSide;
                    y1 = verticalOffset + i * boxSide;
                    x2 = horizontalOffset + (j + 1) * boxSide;
                    y2 = verticalOffset + (i + 1) * boxSide;

                    canvas.drawRect(x1, y1, x2, y2, boxPaint);

                }

                    if (box.top) {
                        x1 = horizontalOffset + j * boxSide;
                        x2 = horizontalOffset + (j + 1) * boxSide;
                        y1 = verticalOffset + i * boxSide;
                        y2 = verticalOffset + i * boxSide;
                        if (gameState==PLAYER1_TURN) {
                            canvas.drawLine(x1, y1, x2, y2, linePaint);
                        }else{
                            canvas.drawLine(x1, y1, x2, y2, linePaint1);

                        }


                    }

                if (box.left) {
                    x1 = horizontalOffset + j * boxSide;
                    x2 = horizontalOffset + j * boxSide;
                    y1 = verticalOffset + i * boxSide;
                    y2 = verticalOffset + (i + 1) * boxSide;


                    if (gameState==PLAYER1_TURN) {
                        canvas.drawLine(x1, y1, x2, y2, linePaint);
                    }else{
                        canvas.drawLine(x1, y1, x2, y2, linePaint1);

                    }
                }

                if (box.right && j == board.getColumns() - 1) {
                    x1 = horizontalOffset + (j + 1) * boxSide;
                    x2 = horizontalOffset + (j + 1) * boxSide;
                    y1 = verticalOffset + i * boxSide;
                    y2 = verticalOffset + (i + 1) * boxSide;

                    if (gameState==PLAYER1_TURN) {
                        canvas.drawLine(x1, y1, x2, y2, linePaint);
                    }else{
                        canvas.drawLine(x1, y1, x2, y2, linePaint1);

                    }

                }

                if (box.bottom && i == board.getRows() - 1) {
                    x1 = horizontalOffset + j * boxSide;
                    x2 = horizontalOffset + (j + 1) * boxSide;
                    y1 = verticalOffset + (i + 1) * boxSide;
                    y2 = verticalOffset + (i + 1) * boxSide;

                    if (gameState==PLAYER1_TURN) {
                        canvas.drawLine(x1, y1, x2, y2, linePaint);
                    }else{
                        canvas.drawLine(x1, y1, x2, y2, linePaint1);

                    }
                }
            }

        for (int i = 0; i <= board.getRows(); i++)
            for (int j = 0; j <= board.getColumns(); j++) {
                x1 = horizontalOffset + j * boxSide;
                y1 = verticalOffset + i * boxSide;
                canvas.drawCircle(x1, y1, dotRadius, dotPaint);
            }

        if (revalidate)
            this.postInvalidate();
    }

    public void setGame(Game game) {
        this.game = game;
        this.boxesAlpha = new int[game.getBoard().getRows()][game.getBoard().getColumns()];
        requestLayout();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 300;
        int desiredHeight = 300;

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = Math.max(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            height = Math.max(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);

        int size;
        if (width < height) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
            size = width - getPaddingLeft() - getPaddingRight();
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
            size = height - getPaddingTop() - getPaddingBottom();
        }

        if (game == null)
            return;

        if (game.getBoard().getColumns() < game.getBoard().getRows()) {
            boxSide = size / game.getBoard().getRows();
        } else {
            boxSide = size / game.getBoard().getColumns();
        }

        this.horizontalOffset = (width - size) / 2;
        this.verticalOffset = (height - size) / 2;

        this.touchWidth = game.getBoard().getColumns() * boxSide + snapLength;
        this.touchHeight = game.getBoard().getRows() * boxSide + snapLength;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!touchable)
            return false;

        if (game == null)
            return false;

        Grid board = game.getBoard();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_MOVE: {
                float touchX = motionEvent.getX() - horizontalOffset;
                float touchY = motionEvent.getY() - verticalOffset;

                if (touchX < -snapLength ||
                        touchX > touchWidth ||
                        touchY < -snapLength ||
                        touchY > touchHeight) {
                    return false;
                }

                float rowY = Math.abs(touchY) / boxSide;
                float columnX = Math.abs(touchX) / boxSide;

                double deltaXLeft, deltaYDown, deltaXRight, deltaYUp, deltaX, deltaY;

                deltaXLeft = columnX - Math.floor(columnX);
                deltaXRight = Math.ceil(columnX) - columnX;
                deltaYDown = rowY - Math.floor(rowY);
                deltaYUp = Math.ceil(rowY) - rowY;

                deltaX = deltaXLeft > deltaXRight ? deltaXRight : deltaXLeft;
                deltaY = deltaYDown > deltaYUp ? deltaYUp : deltaYDown;

                if (deltaX < deltaY) {
                    if (deltaX < snapLength) {
                        x1temp = (float) Math.floor(columnX);
                        x2temp = x1temp;
                        y1temp = (float) Math.floor(rowY);
                        y2temp = y1temp + 1;

                        if (deltaX == deltaXRight) {
                            x1temp += 1;
                            x2temp += 1;
                        }
                        drawTemp = true;
                    } else {
                        x1temp = 0;
                        x2temp = 0;
                        y1temp = 0;
                        y2temp = 0;
                        drawTemp = false;
                    }
                } else if (deltaX >= deltaY) {
                    if (deltaY < snapLength) {
                        x1temp = (float) Math.floor(columnX);
                        x2temp = x1temp + 1;
                        y1temp = (float) Math.floor(rowY);
                        y2temp = y1temp;

                        if (deltaY == deltaYUp) {
                            y1temp += 1;
                            y2temp += 1;
                        }
                        drawTemp = true;
                    } else {
                        x1temp = 0;
                        x2temp = 0;
                        y1temp = 0;
                        y2temp = 0;
                        drawTemp = false;
                    }
                }

                invalidate();
                return true;
            }

            case MotionEvent.ACTION_UP:
                int numberDotStart = ((int) y1temp) * (board.getColumns() + 1) + (int) x1temp;
                int numberDotEnd = ((int) y2temp) * (board.getColumns() + 1) + (int) x2temp;

                if (game.getGameEventListener() != null) {
                    game.getGameEventListener().onPlayerMove(new Lines(
                            numberDotStart,
                            numberDotEnd
                    ));
                }

                invalidate();

            case MotionEvent.ACTION_CANCEL: {
                drawTemp = false;
                x1temp = 0;
                x2temp = 0;
                y1temp = 0;
                y2temp = 0;
                invalidate();
                return true;
            }
        }
        return false;
    }

    public void setTurnMe(boolean turnMe) {
        isTurnMe = turnMe;
        if (isTurnMe) {
            colorPlayer1 = ContextCompat.getColor(getContext(), R.color.player1);
            colorPlayer2 = ContextCompat.getColor(getContext(), R.color.player2);
            colorPlayer11=
                    colorPlayer22=
        } else {
            colorPlayer1 = ContextCompat.getColor(getContext(), R.color.player2);
            colorPlayer2 = ContextCompat.getColor(getContext(), R.color.player1);
        }
    }



    public void enableInteraction() {
        touchable = true;
    }

    public void disableInteraction() {
        touchable = false;
    }

}


