# 可以监听向上或者向下滑动，是否到顶部或者底部，滑动距离，停止。最简单的方法就是最可靠的方法

sc.setScrollStateListener(new XScrollView.ScrollStateListener(

    @Override    public void scrollUp() {        
    Log.i("test","向上滑动");    
    }   
    
    @Override    public void scrollDown() {
    Log.i("test","向下滑动");    
    }    
    
    @Override    public void reachTop() {
    Log.i("test","到顶了");    
    }   
    
    @Override   
    public void reachBottom() {  
    Log.i("test","到底了");   
    }   
    
    @Override    public void onScrolling(int t) {       
    Log.i("test",t+"滑动距离");   
    }   
    
    @Override    public void onStop() {       
    Log.i("test","停止了");    
    }
    
});
