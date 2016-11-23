function thetaMatrix = onlineCalc( A,y,eta)
%Calcluate theta dynamicly;
%Matrix A;
% lambda
% epsilon
 warning('off','all');
[n_a, L] = size(A);
thetaMatrix = zeros(L, n_a-1);
%O = max(size(y));
%y_ = zeros(1,O-1);
c = 0-eye(L);
d = zeros(1,L);
c_eq = ones(1,L);
d_eq = 1;

for i = 2:n_a
    newA = A(1:i, :);
    newY = y(:, 1:i);  
    [new_n, new_L] = size(newA);
    for j = 1:(new_n-1)
        A_(j, :) = A(j+1, :) - A(1,:);
        y_(j) = y(j+1)-y(1);
    end
    [rY, lY] = size(y_);
    [rA, lA] = size(A_);
    mu = ones(rY, lA) ;
    theta = zeros(L, 1);
    theta_old = theta;
    theta = theta_old - eta*((y_'-A_*theta_old)'*(0-A_) + mu)';
    thetaMatrix(:,i-1) = theta;
end
warning('on','all');
end

