

import heapq

def get_seconds(time_str):
    splited_str = time_str.split(':')
    sec_str = splited_str[2].split('.')[0]
    milisec_str = splited_str[2].split('.')[1]
    
    result = int(sec_str) * 1000
    result += int(splited_str[1]) * 60000
    result += int(splited_str[0]) * 3600000
    result += int(milisec_str)
    
    return result

START = 0
FINISH = 1

def solution(lines):
    h = []
    for line in lines:
        splitted_line = line.split()
        seconds = get_seconds(splitted_line[1])
        duration = float(splitted_line[2][:-1]) * 1000
        heapq.heappush(h, [seconds, START])
        heapq.heappush(h, [seconds + duration + 1000 - 1.5, FINISH])
    max_count = 0
    cur_count = 0
    
    for i in range(len(h)):
        item = heapq.heappop(h)
        if item[1] == START:
            cur_count = cur_count + 1
        else:
            cur_count = cur_count - 1
        max_count = max(cur_count, max_count)
    return max_count

def test_get_seconds1():
    assert 1000 == get_seconds('00:00:01.000')

def test_get_seconds2():
    assert 2000 == get_seconds('00:00:02.000')

def test_get_seconds3():
    assert 2001 == get_seconds('00:00:02.001')

def test_get_seconds4():
    assert 62001 == get_seconds('00:01:02.001')

def test_get_seconds4():
    assert 3662001 == get_seconds('01:01:02.001')

def test_1():
    input = [ '2016-09-15 01:00:04.001 2.0s', '2016-09-15 01:00:07.000 2s' ]
    assert 1 == solution(input)

def test_2():
    input = [ '2016-09-15 01:00:07.000 2s' , '2016-09-15 01:00:04.001 2.0s']
    assert 1 == solution(input)

def test_3():
    input = [ '2016-09-15 01:00:04.002 2.0s', '2016-09-15 01:00:07.000 2s' ]
    assert 2 == solution(input)

def test_4():
    input = [ '2016-09-15 20:59:57.421 0.351s', '2016-09-15 20:59:58.233 1.181s', '2016-09-15 20:59:58.299 0.8s', '2016-09-15 20:59:58.688 1.041s', '2016-09-15 20:59:59.591 1.412s', '2016-09-15 21:00:00.464 1.466s', '2016-09-15 21:00:00.741 1.581s', '2016-09-15 21:00:00.748 2.31s', '2016-09-15 21:00:00.966 0.381s', '2016-09-15 21:00:02.066 2.62s' ] 
    # assert 7 == solution(input)
    