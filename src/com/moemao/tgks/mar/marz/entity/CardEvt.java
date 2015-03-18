package com.moemao.tgks.mar.marz.entity;

/**
 * 
 * 项目名称：TGKS_MAR
 * 类名称：CardEvt
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2015-3-14 上午11:25:13
 * 修改人：Administrator
 * 修改时间：2015-3-14 上午11:25:13
 * 修改备注：
 * @version  
 *  
    cards       0
        uniqid              0
        cardid              1
        lv                     2
        lv_max             3
        exp                  4
        love                 5
        skill_lv              6
        hp                    7
        atkp                 8
        intp                  9
        mndp               10
        next_lv_exp       11
        now_lv_exp       12
        add_exp            13
        base_add_price 14
        evolution_price 15
        is_lock              16
        create_time      17
        fame                18
 */
public class CardEvt
{
    private String uniqid;
    private String cardid;
    private int lv;
    private int lv_max;
    private int exp;
    private int love;
    private String skill_lv;
    private int hp;
    private int atkp;
    private int intp;
    private int mndp;
    private int next_lv_exp;
    private int now_lv_exp;
    private int add_exp;
    private int base_add_price;
    private int evolution_price;
    private int is_lock;
    private int create_time;
    private int fame;
    
    public String getUniqid()
    {
        return uniqid;
    }
    public void setUniqid(String uniqid)
    {
        this.uniqid = uniqid;
    }
    public String getCardid()
    {
        return cardid;
    }
    public void setCardid(String cardid)
    {
        this.cardid = cardid;
    }
    public int getLv()
    {
        return lv;
    }
    public void setLv(int lv)
    {
        this.lv = lv;
    }
    public int getLv_max()
    {
        return lv_max;
    }
    public void setLv_max(int lv_max)
    {
        this.lv_max = lv_max;
    }
    public int getExp()
    {
        return exp;
    }
    public void setExp(int exp)
    {
        this.exp = exp;
    }
    public int getLove()
    {
        return love;
    }
    public void setLove(int love)
    {
        this.love = love;
    }
    public String getSkill_lv()
    {
        return skill_lv;
    }
    public void setSkill_lv(String skill_lv)
    {
        this.skill_lv = skill_lv;
    }
    public int getHp()
    {
        return hp;
    }
    public void setHp(int hp)
    {
        this.hp = hp;
    }
    public int getAtkp()
    {
        return atkp;
    }
    public void setAtkp(int atkp)
    {
        this.atkp = atkp;
    }
    public int getIntp()
    {
        return intp;
    }
    public void setIntp(int intp)
    {
        this.intp = intp;
    }
    public int getMndp()
    {
        return mndp;
    }
    public void setMndp(int mndp)
    {
        this.mndp = mndp;
    }
    public int getNext_lv_exp()
    {
        return next_lv_exp;
    }
    public void setNext_lv_exp(int next_lv_exp)
    {
        this.next_lv_exp = next_lv_exp;
    }
    public int getNow_lv_exp()
    {
        return now_lv_exp;
    }
    public void setNow_lv_exp(int now_lv_exp)
    {
        this.now_lv_exp = now_lv_exp;
    }
    public int getAdd_exp()
    {
        return add_exp;
    }
    public void setAdd_exp(int add_exp)
    {
        this.add_exp = add_exp;
    }
    public int getBase_add_price()
    {
        return base_add_price;
    }
    public void setBase_add_price(int base_add_price)
    {
        this.base_add_price = base_add_price;
    }
    public int getEvolution_price()
    {
        return evolution_price;
    }
    public void setEvolution_price(int evolution_price)
    {
        this.evolution_price = evolution_price;
    }
    public int getIs_lock()
    {
        return is_lock;
    }
    public void setIs_lock(int is_lock)
    {
        this.is_lock = is_lock;
    }
    public int getCreate_time()
    {
        return create_time;
    }
    public void setCreate_time(int create_time)
    {
        this.create_time = create_time;
    }
    public int getFame()
    {
        return fame;
    }
    public void setFame(int fame)
    {
        this.fame = fame;
    }
}
