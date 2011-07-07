//
/*
* ListStyleButtonField.java
*
* Research In Motion Limited proprietary and confidential
* Copyright Research In Motion Limited, 2008-2008
*/

package com.atencion24.interfaz;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;

    
public class ListStyleLabelField extends Field
{
    public static int DRAWPOSITION_TOP = 0;
    public static int DRAWPOSITION_BOTTOM = 1;
    public static int DRAWPOSITION_MIDDLE = 2;
    public static int DRAWPOSITION_SINGLE = 3;
    
    private static final int CORNER_RADIUS = 18;
    
    private static final int HPADDING = 6; //Display.getWidth() <= 320 ? 6 : 8;
    private static final int VPADDING = 4;
    
    private int COLOR_BACKGROUND = 0x600808;
    private int COLOR_BORDER = 0xBBBBBB;
    private int COLOR_BACKGROUND_FOCUS = 0x600808;
    
    
    private MyLabelField _labelField;
    private Bitmap _leftIcon;
    private Bitmap _actionIcon;
    
    private int _targetHeight;
    private int _rightOffset;
    private int _leftOffset;
    private int _labelHeight;
    
    private int _drawPosition = -1;

    public ListStyleLabelField( String label, long style )
    {
        this( label, null, style );
    }
    
    public ListStyleLabelField( String label, long style, int Color, int colorL )
    {
        
        super( USE_ALL_WIDTH | Field.FOCUSABLE );
               
        _labelField = new MyLabelField( label, style, colorL );
        this.COLOR_BACKGROUND = Color;
        this.COLOR_BACKGROUND_FOCUS = Color;
        _actionIcon = null;
        _leftIcon = null;
        
    }
    
    
    public ListStyleLabelField( String label, Bitmap actionIcon )
    {
        this( label, actionIcon, 0 );
    }
    
    public ListStyleLabelField( String label, Bitmap actionIcon, long style )
    {
        this( null, label, actionIcon, style );
    }
    
    public ListStyleLabelField( Bitmap icon, String label, long style )
    {
        this( icon, label, null, style );
    }
    
    public ListStyleLabelField( Bitmap icon, String label, Bitmap actionIcon, long style )
    {
        super( USE_ALL_WIDTH | Field.FOCUSABLE );
               
        _labelField = new MyLabelField( label, style );
        _actionIcon = actionIcon;
        _leftIcon = icon;
    }
    
    /**
* DRAWPOSITION_TOP | DRAWPOSITION_BOTTOM | DRAWPOSITION_MIDDLE
* Determins how the field is drawn (borders)
* If none is set, then no borders are drawn
*/
    public void setDrawPosition( int drawPosition )
    {
        _drawPosition = drawPosition;
    }
    
    public String toString()
    {
        return _labelField.toString();
    }
    
    public void layout( int width, int height )
    {

        _targetHeight = getFont().getHeight() / 2 * 3 + 2 * VPADDING;
//#ifndef VER_4.6.1 | VER_4.6.0 | VER_4.5.0 | VER_4.2.1 | VER_4.2.0
       /* if( Touchscreen.isSupported() ) {
_targetHeight = getFont().getHeight() * 2 + 2 * VPADDING;
}*/
//#endif
        
        _leftOffset = HPADDING;
        if( _leftIcon != null ) {
            _leftOffset += _leftIcon.getWidth() + HPADDING;
        }
        
        _rightOffset = HPADDING;
        if( _actionIcon != null ) {
            _rightOffset += _actionIcon.getWidth() + HPADDING;
        }
        
        _labelField.layout( width - _leftOffset - _rightOffset, height );
        _labelHeight = _labelField.getHeight();
        int labelWidth = _labelField.getWidth();
        
        if( _labelField.isStyle( DrawStyle.HCENTER ) ) {
            _leftOffset = ( width - labelWidth ) / 2;
        } else if ( _labelField.isStyle( DrawStyle.RIGHT ) ) {
            _leftOffset = width - labelWidth - HPADDING - _rightOffset;
        }
        
        int extraVPaddingNeeded = 0;
        if( _labelHeight < _targetHeight ) {
            // Make sure that they are at least 1.5 times font height
            extraVPaddingNeeded = ( _targetHeight - _labelHeight ) / 2;
        }
        
        setExtent( width, _labelHeight + 2 * extraVPaddingNeeded );
    }
    
    public void setText( String text )
    {
        _labelField.setText( text );
        updateLayout();
    }
    
    protected void paint( Graphics g )
    {
        // Left Bitmap
        if( _leftIcon != null ) {
            g.drawBitmap( HPADDING, VPADDING, _leftIcon.getWidth(), _leftIcon.getHeight(), _leftIcon, 0, 0 );
        }
        
        // Text
        try {
            g.pushRegion( _leftOffset, ( getHeight() - _labelHeight ) / 2, getWidth() - _leftOffset - _rightOffset, _labelHeight, 0, 0 );
            _labelField.paint( g );
        } finally {
            g.popContext();
        }
        
        // Right (Action) Bitmap
        if( _actionIcon != null ) {
            g.drawBitmap( getWidth() - HPADDING - _actionIcon.getWidth(), ( getHeight() - _actionIcon.getHeight() ) / 2, _actionIcon.getWidth(), _actionIcon.getHeight(), _actionIcon, 0, 0 );
        }
    }
    
    protected void paintBackground( Graphics g )
    {
        if( _drawPosition < 0 ) {
            // it's like a list field, let the default background be drawn
            super.paintBackground( g );
            return;
        }
        
        int oldColour = g.getColor();
        
        int background = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ? COLOR_BACKGROUND_FOCUS : COLOR_BACKGROUND;
        try {
            if( _drawPosition == 0 ) {
                // Top
                g.setColor( background );
                g.fillRoundRect( 0, 0, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS );
                g.setColor( COLOR_BORDER );
                g.drawRoundRect( 0, 0, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS );
                g.drawLine( 0, getHeight() - 1, getWidth(), getHeight() - 1 );
            } else if( _drawPosition == 1 ) {
                // Bottom
                g.setColor( background );
                g.fillRoundRect( 0, -CORNER_RADIUS, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS );
                g.setColor( COLOR_BORDER );
                g.drawRoundRect( 0, -CORNER_RADIUS, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS );
            } else if( _drawPosition == 2 ) {
                // Middle
                g.setColor( background );
                g.fillRoundRect( 0, -CORNER_RADIUS, getWidth(), getHeight() + 2 * CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS );
                g.setColor( COLOR_BORDER );
                g.drawRoundRect( 0, -CORNER_RADIUS, getWidth(), getHeight() + 2 * CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS );
                //g.drawLine( 0, getHeight() - 1, getWidth(), getHeight() - 1 );
            } else {
                // Single
                g.setColor( background );
                g.fillRoundRect( 0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS );
                g.setColor( COLOR_BORDER );
                g.drawRoundRect( 0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS );
            }
        } finally {
            g.setColor( oldColour );
        }
    }
    
    protected void drawFocus( Graphics g, boolean on )
    {
        if( _drawPosition < 0 ) {
            super.drawFocus( g, on );
        } else {
            boolean oldDrawStyleFocus = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS );
            try {
                if( on ) {
                    g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, true );
                }
                paintBackground( g );
                paint( g );
            } finally {
                g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus );
            }
        }
    }
    
         
    /**
* A public way to click this button
*/
       
/*#ifndef VER_4.6.1 | VER_4.6.0 | VER_4.5.0 | VER_4.2.1 | VER_4.2.0
protected boolean touchEvent( TouchEvent message )
{
int x = message.getX( 1 );
int y = message.getY( 1 );
if( x < 0 || y < 0 || x > getExtent().width || y > getExtent().height ) {
// Outside the field
return false;
}
switch( message.getEvent() ) {
case TouchEvent.UNCLICK:
clickButton();
return true;
}
return super.touchEvent( message );
}
//#endif */

    public void setDirty( boolean dirty ) {}
    public void setMuddy( boolean muddy ) {}
    
    
    private static class MyLabelField extends LabelField
    {
        public int color = 0xFFFFFF;
        public MyLabelField( String text, long style )
        {
            super( text, style );
        }
        public MyLabelField( String text, long style, int color )
        {
            super( text, style );
            this.color = color;
        }
    
        public void layout( int width, int height )
        {
            super.layout( width, height );
        }
        
        public void paint( Graphics g )
        {
            g.setColor( color );
            super.paint( g );
        }
    }
}

