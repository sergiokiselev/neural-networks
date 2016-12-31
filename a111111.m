fileId = fopen('output.txt');

[a, b] = textscan(fileId, '%f %f');
fclose(fileId);
k = a{1}
k;
b = a{2};

%k = -0.17952815086879995;
%b = 6.604862536676114;
x = [-10, 10];
y = k * x + b;


hold on
plot(-9,1,'*');
plot(9,-1,'*');
plot(-1,-9,'*');
plot(1, 9, 'r*');

plot(x, y);

%# vertical line
hx = graph2d.constantline(0, 'LineStyle',':', 'Color',[.7 .7 .7]);
changedependvar(hx,'x');

%# horizontal line
hy = graph2d.constantline(0, 'Color',[.7 .7 .7]);
changedependvar(hy,'y');
