local rs = redis.call('get',KEYS[1])
if rs==ARGV[1] then
   return redis.call('del',KEYS[1])
end
return rs
