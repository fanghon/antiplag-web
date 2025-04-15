package cn.mooyyu.antiplagweb.pojo;

import lombok.Data;

@Data
public class SimData implements Comparable<SimData> {
    String file1;
    String file2;
    float similar;
    String url;

    @Override
    public int compareTo(SimData o) {
        return Float.compare(this.similar, o.getSimilar());
    }
}
