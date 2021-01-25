package com.bin.david.form.data.format.sequence;



/**
 * Created by huang on 2017/11/7.
 */

public class NumberSequenceFormat extends BaseSequenceFormat{

    @Override
    public String format(Integer position,int... pos) {
        return String.valueOf(position);
    }


}
