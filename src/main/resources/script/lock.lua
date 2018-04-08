local expire = tonumber(ARGV[2])
local rs = redis.call('setnx',KEYS[1],ARGV[1])
if rs==1 then
   redis.call('expire',KEYS[1],expire)
   return 1
end
local val = redis.call('get',KEYS[1])
if val==ARGV[1] then
  redis.call('expire',KEYS[1],expire)
  return 1
end
return 0
