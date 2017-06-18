package com.android.divgarg.blockbustermovies.models;

/**
 * Created by divgarg on 6/17/17.
 */

public enum MenuItemTypes
{
    POPULAR (0),
    TOP_RATED (1),
    FAVOURITE (2);

    private int menuType;

    MenuItemTypes(int type)
    {
        menuType = type;
    }

    public int getMenuType()
    {
        return menuType;
    }

    public static MenuItemTypes getType(final int selector){
        MenuItemTypes type = POPULAR;

        switch (selector)
        {
            case 0:
            {
                type =  POPULAR;
                break;
            }
            case 1:
            {
                type = TOP_RATED;
                break;
            }
            case 2:
            {
                type = FAVOURITE;
                break;
            }
        }
        return type;
    }

}
