use "lang/storage" as storage
o = storage.loadYaml("conf.yml")   #{"a" = 1, "b" = 2}
for k, v in o.pairs()
    print(k, v)
#