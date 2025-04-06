import numpy as np

def AGC(x, level, max_gain, win_length):
    # 初始化
    agc_in = np.concatenate((np.zeros(win_length), x))  # 加上win_length个0作为前缀
    agc_out = []
    
    agc_total = 0
    agc_mean = 0
    agc_gain = 0

    target_level = level / np.sqrt(2)  # 目标幅度
    
    for m in range(len(x)):
        # 计算AGC总量，累加的总值
        agc_total += abs(agc_in[m]) - abs(agc_in[m + win_length])
        
        # 计算均值
        agc_mean = agc_total / win_length
        
        # 计算增益
        agc_gain = min(max_gain, target_level / agc_mean)
        
        # 计算输出
        agc_out.append(agc_in[m + win_length] * agc_gain)

    # 返回增益调整后的信号
    return np.array(agc_out)