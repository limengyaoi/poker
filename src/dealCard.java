import java.awt.*;

public class dealCard implements Runnable{
    public Main main;

    public dealCard(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        int i=1;
        while(true){
            synchronized (main) {
                    if(i<=54){
                            System.out.println(Thread.currentThread().getName()+"创建发牌线程");
                            int t = 0;
                            for ( i = 1; i <= 54; i++) {
                                if (i >= 52) {
                                    Common.move(main.card[i], main.card[i].getLocation(), new Point(300 + (i - 52) * 80, 10));
                                    main. lordList.add(main.card[i]);
                                    continue;
                                }
                                switch ((t++) % 3) {
                                    case 0:
                                        Common.move(main.card[i], main.card[i].getLocation(), new Point(50, 60 + i * 5));
                                        main.playerList[0].add(main.card[i]);
                                        // main.card[i].turnFront();
                                        break;
                                    case 1:
                                        Common.move(main.card[i], main.card[i].getLocation(), new Point(180 + i * 7, 450));
                                        main.playerList[1].add(main.card[i]);
                                        main.card[i].turnFront();
                                        break;
                                    case 2:
                                        Common.move(main.card[i],main. card[i].getLocation(), new Point(700, 60 + i * 5));
                                        main.playerList[2].add(main.card[i]);
                                        //main.card[i].turnFront();
                                        break;
                                }
                                main.container.setComponentZOrder(main.card[i], 0);
                            }

                    }
                    else{
                        for (int m = 0; m  < 3; m++) {
                            Common.order(main.playerList[m]);
                            Common.rePosition(main, main.playerList[m], m);
                        }
                        System.out.println(Thread.currentThread().getName()+"发牌线程结束");
                        try {
                            Thread.sleep(10);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
            }
        }



    }
}
